package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.RovioViewModel
import com.example.ui.components.AuditTab
import com.example.ui.components.BottomNavTabBar
import com.example.ui.components.DailySummaryTab
import com.example.ui.components.EditTitleDialog
import com.example.ui.components.HistoryDrawer
import com.example.ui.components.HitEffectOverlay
import com.example.ui.components.ThemeSelectionDialog
import com.example.ui.components.TimeVaultTab
import com.example.ui.theme.AppTheme

@Composable
fun MainScreen(
    viewModel: RovioViewModel
) {
    val colors = AppTheme.colors

    val streak by viewModel.streak.collectAsStateWithLifecycle()
    val freezeShields by viewModel.freezeShields.collectAsStateWithLifecycle()
    val todaySlots by viewModel.todaySlots.collectAsStateWithLifecycle()
    val archiveDays by viewModel.archiveDays.collectAsStateWithLifecycle()
    val rushAttempts by viewModel.rushAttempts.collectAsStateWithLifecycle()
    val stackedAttempts by viewModel.stackedAttempts.collectAsStateWithLifecycle()
    val selectedThemeIndex by viewModel.selectedThemeIndex.collectAsStateWithLifecycle()

    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedArchiveDay by viewModel.selectedArchiveDay.collectAsStateWithLifecycle()
    val editingSlot by viewModel.editingSlot.collectAsStateWithLifecycle()
    val showHitEffect by viewModel.showHitEffect.collectAsStateWithLifecycle()
    val vaultToastMessage by viewModel.vaultToastMessage.collectAsStateWithLifecycle()

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.desk)
            .statusBarsPadding()
            .testTag("main_screen"),
        bottomBar = {
            BottomNavTabBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        containerColor = colors.desk
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    TodayScreen(
                        streak = streak,
                        freezeShields = freezeShields,
                        slots = todaySlots,
                        archiveDays = archiveDays,
                        showHitEffect = showHitEffect,
                        onSlotClick = { viewModel.handleStatusClick(it) },
                        onTitleClick = { viewModel.openEditDialog(it) },
                        onHoldDoneConfirmed = { viewModel.confirmDoneHoldPassed(it) },
                        onArchiveDayClick = { viewModel.selectArchiveDay(it) },
                        onThemeClick = { showThemeDialog = true }
                    )
                }
                1 -> {
                    DailySummaryTab(
                        slots = todaySlots,
                        streak = streak,
                        onSlotClick = { viewModel.handleStatusClick(it) },
                        onTitleClick = { viewModel.openEditDialog(it) }
                    )
                }
                2 -> {
                    TimeVaultTab(
                        slots = todaySlots,
                        rushAttempts = rushAttempts,
                        stackedAttempts = stackedAttempts,
                        toastMessage = vaultToastMessage,
                        onClearToast = { viewModel.clearVaultToast() }
                    )
                }
                3 -> {
                    AuditTab(
                        archiveDays = archiveDays
                    )
                }
            }

            // Hit Effect Confetti Overlay
            if (showHitEffect) {
                HitEffectOverlay(modifier = Modifier.fillMaxSize())
            }

            // History Drawer Overlay
            if (selectedArchiveDay != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HistoryDrawer(
                        day = selectedArchiveDay!!,
                        onClose = { viewModel.selectArchiveDay(null) }
                    )
                }
            }

            // Edit Title Dialog
            if (editingSlot != null) {
                EditTitleDialog(
                    slot = editingSlot!!,
                    onDismiss = { viewModel.closeEditDialog() },
                    onSave = { newTitle ->
                        viewModel.saveSlotTitle(editingSlot!!.id, newTitle)
                    }
                )
            }

            // 7 New UI & Themes Preview Dialog
            if (showThemeDialog) {
                ThemeSelectionDialog(
                    selectedThemeIndex = selectedThemeIndex,
                    onSelectTheme = { index ->
                        viewModel.selectTheme(index)
                    },
                    onDismiss = { showThemeDialog = false }
                )
            }
        }
    }
}
