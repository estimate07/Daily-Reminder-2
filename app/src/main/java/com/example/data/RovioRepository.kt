package com.example.data

import com.example.util.IstTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

sealed class OperationResult {
    object Success : OperationResult()
    data class RushBlocked(val slot: Int, val earlyByMs: Long) : OperationResult()
    data class CooldownBlocked(val slot: Int, val waitLeftMs: Long) : OperationResult()
    data class Error(val message: String) : OperationResult()
}

class RovioRepository(
    private val shortDao: ShortDao,
    private val userPreferencesManager: UserPreferencesManager
) {
    val todaySlots: Flow<List<ShortSlot>> = shortDao.getTodaySlots()
    val archiveDays: Flow<List<ArchiveDay>> = shortDao.getArchiveDays()
    val rushAttempts: Flow<List<RushAttempt>> = shortDao.getRushAttempts()
    val stackedAttempts: Flow<List<StackedAttempt>> = shortDao.getStackedAttempts()

    val streak: Flow<Int> = userPreferencesManager.streak
    val freezeShields: Flow<Int> = userPreferencesManager.freezeShields

    suspend fun initializeAndCheckReset() {
        val currentIstDate = IstTimeUtils.getCurrentIstDateString()
        val lastResetDate = userPreferencesManager.lastResetDate.first()

        // 1. Initial seed for slots if empty
        var currentSlots = shortDao.getTodaySlotsList()
        if (currentSlots.isEmpty()) {
            val initialSlots = listOf(
                ShortSlot(id = 1, title = "", status = "TODO", label = "MORNING"),
                ShortSlot(id = 2, title = "", status = "TODO", label = "AFTERNOON"),
                ShortSlot(id = 3, title = "", status = "TODO", label = "EVENING")
            )
            shortDao.insertOrUpdateSlots(initialSlots)
            currentSlots = initialSlots
        }

        // 2. Initial seed for 30 archive days if empty
        val currentArchive = shortDao.getArchiveDays().first()
        if (currentArchive.isEmpty()) {
            val mockArchive = mutableListOf<ArchiveDay>()
            // Generate 30 days of past history
            for (i in 1..30) {
                val dayNum = i
                val dateLabel = "DAY $i"
                val count = when {
                    i <= 14 -> 3 // Mock 14 streak days fully done
                    i % 3 == 0 -> 2
                    i % 4 == 0 -> 1
                    else -> 0
                }
                val titles = when (count) {
                    3 -> "Morning VLOG|Tech Review|Night Thoughts"
                    2 -> "Quick Workout|Daily Shorts"
                    1 -> "Intro Clip"
                    else -> ""
                }
                val durations = when (count) {
                    3 -> "3720000|3850000|4100000" // ~62m, 64m, 68m
                    2 -> "3650000|3700000|0"
                    1 -> "3900000|0|0"
                    else -> "0|0|0"
                }
                mockArchive.add(ArchiveDay(date = dateLabel, dayNumber = dayNum, count = count, titles = titles, realDurations = durations))
            }
            shortDao.insertArchiveDays(mockArchive)
        }

        // 3. Check for 1AM IST reset
        if (lastResetDate.isNotEmpty() && lastResetDate != currentIstDate) {
            performDailyReset(lastResetDate)
        }
        if (lastResetDate.isEmpty()) {
            userPreferencesManager.setLastResetDate(currentIstDate)
        }
    }

    private suspend fun performDailyReset(previousDate: String) {
        val slots = shortDao.getTodaySlotsList()
        val doneCount = slots.count { it.status == "DONE" }
        val currentStreak = userPreferencesManager.streak.first()
        val currentShields = userPreferencesManager.freezeShields.first()

        if (doneCount == 3) {
            userPreferencesManager.setStreak(currentStreak + 1)
        } else {
            if (currentShields > 0) {
                // Use freeze shield
                userPreferencesManager.setFreezeShields(currentShields - 1)
            } else {
                // Reset streak
                userPreferencesManager.setStreak(0)
            }
        }

        // Save to Archive
        val archiveCount = (shortDao.getArchiveDays().first().size % 30) + 1
        val titlesStr = slots.joinToString("|") { it.title.ifEmpty { "Short ${it.id}" } }
        val durationsStr = slots.joinToString("|") {
            if (it.startTime != null && it.doneTime != null) {
                (it.doneTime - it.startTime).toString()
            } else "0"
        }

        shortDao.insertArchiveDay(
            ArchiveDay(
                date = IstTimeUtils.getFormattedDateLabel(previousDate),
                dayNumber = archiveCount,
                count = doneCount,
                titles = titlesStr,
                realDurations = durationsStr
            )
        )

        // Clear today's slots
        val resetSlots = listOf(
            ShortSlot(id = 1, title = "", status = "TODO", label = "MORNING"),
            ShortSlot(id = 2, title = "", status = "TODO", label = "AFTERNOON"),
            ShortSlot(id = 3, title = "", status = "TODO", label = "EVENING")
        )
        shortDao.insertOrUpdateSlots(resetSlots)
        userPreferencesManager.setLastResetDate(IstTimeUtils.getCurrentIstDateString())
    }

    suspend fun updateSlotTitle(slotId: Int, newTitle: String) {
        val slots = shortDao.getTodaySlotsList()
        val existing = slots.find { it.id == slotId } ?: return

        var updatedStartTime = existing.startTime
        var updatedStatus = existing.status

        // Edge case: If user deletes title within 1 hour of ARM, reset startTime to null
        if (newTitle.isBlank() && existing.status == "DOING" && existing.startTime != null) {
            val elapsed = System.currentTimeMillis() - existing.startTime
            if (elapsed < 3600000L) {
                updatedStartTime = null
                updatedStatus = "TODO"
            }
        }

        val updated = existing.copy(
            title = newTitle.trim(),
            startTime = updatedStartTime,
            status = updatedStatus
        )
        shortDao.insertOrUpdateSlot(updated)
    }

    suspend fun armSlot(slotId: Int): OperationResult {
        val now = System.currentTimeMillis()
        val lastArm = userPreferencesManager.lastArmTime.first()
        val cooldownMs = 900000L // 15 minutes = 15 * 60 * 1000 = 900,000 ms

        // Check if there is another slot currently armed or last arm time was within 15 min
        if (lastArm > 0 && (now - lastArm) < cooldownMs) {
            val waitLeft = cooldownMs - (now - lastArm)
            // Find which slot was armed
            val slots = shortDao.getTodaySlotsList()
            val activeDoingSlot = slots.find { it.status == "DOING" }?.id ?: 1
            val attempt = StackedAttempt(
                slot = slotId,
                fromSlot = activeDoingSlot,
                time = now,
                waitLeft = waitLeft
            )
            shortDao.insertStackedAttempt(attempt)
            return OperationResult.CooldownBlocked(slotId, waitLeft)
        }

        // Proceed to ARM
        val slots = shortDao.getTodaySlotsList()
        val target = slots.find { it.id == slotId } ?: return OperationResult.Error("Slot not found")

        val updated = target.copy(
            status = "DOING",
            startTime = now
        )
        shortDao.insertOrUpdateSlot(updated)
        userPreferencesManager.setLastArmTime(now)
        return OperationResult.Success
    }

    suspend fun attemptConfirmDone(slotId: Int): OperationResult {
        val now = System.currentTimeMillis()
        val slots = shortDao.getTodaySlotsList()
        val target = slots.find { it.id == slotId } ?: return OperationResult.Error("Slot not found")

        if (target.status != "DOING") return OperationResult.Error("Slot is not in DOING state")

        val startTime = target.startTime ?: now
        val elapsed = now - startTime
        val requiredTimeMs = 3600000L // 1 hour = 3,600,000 ms

        if (elapsed < requiredTimeMs) {
            val earlyBy = requiredTimeMs - elapsed
            val attempt = RushAttempt(
                slot = slotId,
                time = now,
                earlyBy = earlyBy
            )
            shortDao.insertRushAttempt(attempt)
            return OperationResult.RushBlocked(slotId, earlyBy)
        }

        // Hard gate passed! Mark DONE
        val updated = target.copy(
            status = "DONE",
            doneTime = now
        )
        shortDao.insertOrUpdateSlot(updated)
        return OperationResult.Success
    }
}
