package com.example.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ArchiveDay
import com.example.data.OperationResult
import com.example.data.RovioRepository
import com.example.data.RushAttempt
import com.example.data.ShortSlot
import com.example.data.StackedAttempt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RovioViewModel(
    private val repository: RovioRepository,
    private val context: Context
) : ViewModel() {

    val todaySlots: StateFlow<List<ShortSlot>> = repository.todaySlots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archiveDays: StateFlow<List<ArchiveDay>> = repository.archiveDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rushAttempts: StateFlow<List<RushAttempt>> = repository.rushAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stackedAttempts: StateFlow<List<StackedAttempt>> = repository.stackedAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streak: StateFlow<Int> = repository.streak
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 14)

    val freezeShields: StateFlow<Int> = repository.freezeShields
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    private val _selectedTab = MutableStateFlow(0) // 0: TODAY, 1: TIME VAULT, 2: AUDIT
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedArchiveDay = MutableStateFlow<ArchiveDay?>(null)
    val selectedArchiveDay: StateFlow<ArchiveDay?> = _selectedArchiveDay.asStateFlow()

    private val _editingSlot = MutableStateFlow<ShortSlot?>(null)
    val editingSlot: StateFlow<ShortSlot?> = _editingSlot.asStateFlow()

    private val _showHitEffect = MutableStateFlow(false)
    val showHitEffect: StateFlow<Boolean> = _showHitEffect.asStateFlow()

    private val _vaultToastMessage = MutableStateFlow<String?>(null)
    val vaultToastMessage: StateFlow<String?> = _vaultToastMessage.asStateFlow()

    private var previousDoneCount = 0

    init {
        viewModelScope.launch {
            repository.initializeAndCheckReset()
        }

        viewModelScope.launch {
            todaySlots.collect { slots ->
                val currentDoneCount = slots.count { it.status == "DONE" }
                if (currentDoneCount == 3 && previousDoneCount < 3) {
                    triggerHitEffect()
                }
                previousDoneCount = currentDoneCount
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun selectArchiveDay(day: ArchiveDay?) {
        _selectedArchiveDay.value = day
    }

    fun openEditDialog(slot: ShortSlot) {
        _editingSlot.value = slot
    }

    fun closeEditDialog() {
        _editingSlot.value = null
    }

    fun saveSlotTitle(slotId: Int, newTitle: String) {
        viewModelScope.launch {
            repository.updateSlotTitle(slotId, newTitle)
            _editingSlot.value = null
        }
    }

    fun handleStatusClick(slot: ShortSlot) {
        viewModelScope.launch {
            when (slot.status) {
                "TODO" -> {
                    val result = repository.armSlot(slot.id)
                    handleOperationResult(result)
                }
                "DOING" -> {
                    // Attempting second tap / hold confirm
                    val result = repository.attemptConfirmDone(slot.id)
                    handleOperationResult(result)
                }
                "DONE" -> {
                    // Already done
                }
            }
        }
    }

    fun confirmDoneHoldPassed(slotId: Int) {
        viewModelScope.launch {
            val result = repository.attemptConfirmDone(slotId)
            handleOperationResult(result)
        }
    }

    private fun handleOperationResult(result: OperationResult) {
        when (result) {
            is OperationResult.Success -> {
                vibrateSuccess()
            }
            is OperationResult.RushBlocked -> {
                vibrateLight()
                val minutesLeft = (result.earlyByMs / 60000) + 1
                _vaultToastMessage.value = "Too early - ${minutesLeft}m left"
            }
            is OperationResult.CooldownBlocked -> {
                vibrateLight()
                val minutesLeft = (result.waitLeftMs / 60000) + 1
                _vaultToastMessage.value = "Slot 0${result.slot} locked - ${minutesLeft}m cooldown"
            }
            is OperationResult.Error -> {
                vibrateLight()
                _vaultToastMessage.value = result.message
            }
        }
    }

    fun clearVaultToast() {
        _vaultToastMessage.value = null
    }

    private fun triggerHitEffect() {
        _showHitEffect.value = true
        vibrateHitEffect()
        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            _showHitEffect.value = false
        }
    }

    private fun vibrateSuccess() {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(50)
            }
        } catch (e: Exception) {
            // Ignore if vibration not supported
        }
    }

    private fun vibrateLight() {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun vibrateHitEffect() {
        try {
            val vibrator = getVibrator()
            val pattern = longArrayOf(0, 30, 20, 30)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}

class RovioViewModelFactory(
    private val repository: RovioRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RovioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RovioViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
