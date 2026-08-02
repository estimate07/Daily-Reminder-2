package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RushAttempt
import com.example.data.ShortSlot
import com.example.data.StackedAttempt
import com.example.ui.theme.AppTheme
import com.example.ui.theme.InstrumentSerif
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono
import com.example.util.IstTimeUtils
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TimeVaultTab(
    slots: List<ShortSlot>,
    rushAttempts: List<RushAttempt>,
    stackedAttempts: List<StackedAttempt>,
    toastMessage: String?,
    onClearToast: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3500)
            onClearToast()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("time_vault_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Toast banner inside Time Vault
        if (toastMessage != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.signalRed, RoundedCornerShape(8.dp))
                        .border(1.5.dp, colors.ink, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = toastMessage,
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.paper
                    )
                }
            }
        }

        // Section Title: TIME VAULT & HIDDEN TIMERS
        item {
            Text(
                text = "TIME VAULT — HIDDEN WORK LOCKS",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colors.paper,
                letterSpacing = 0.8.sp
            )
        }

        // Live Timer Status Cards
        items(slots) { slot ->
            SlotVaultCard(slot = slot, currentTime = currentTime)
        }

        // Deep Work Total Section
        item {
            DeepWorkSection(slots = slots, currentTime = currentTime)
        }

        // Rush Attempts Section
        if (rushAttempts.isNotEmpty()) {
            item {
                Text(
                    text = "RUSH ATTEMPTS LOG (${rushAttempts.size})",
                    fontFamily = Inter,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText,
                    letterSpacing = 0.8.sp
                )
            }
            items(rushAttempts.take(5)) { rush ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.paper, RoundedCornerShape(6.dp))
                        .border(1.5.dp, colors.ink, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Slot 0${rush.slot} — Blocked Rush",
                                fontFamily = Inter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.ink
                            )
                            val earlyMin = (rush.earlyBy / 60000) + 1
                            Text(
                                text = "Attempted ${earlyMin}m early",
                                fontFamily = JetBrainsMono,
                                fontSize = 10.sp,
                                color = colors.signalRed
                            )
                        }
                        Text(
                            text = IstTimeUtils.formatShortTime(rush.time),
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp,
                            color = colors.mutedText
                        )
                    }
                }
            }
        }

        // Stacked Cooldown Attempts Section
        if (stackedAttempts.isNotEmpty()) {
            item {
                Text(
                    text = "COOLDOWN LOCKS (${stackedAttempts.size})",
                    fontFamily = Inter,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText,
                    letterSpacing = 0.8.sp
                )
            }
            items(stackedAttempts.take(5)) { stacked ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.paper, RoundedCornerShape(6.dp))
                        .border(1.5.dp, colors.ink, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Slot 0${stacked.slot} Cooldown Active",
                                fontFamily = Inter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.ink
                            )
                            val waitMin = (stacked.waitLeft / 60000) + 1
                            Text(
                                text = "${waitMin}m cooldown remaining",
                                fontFamily = JetBrainsMono,
                                fontSize = 10.sp,
                                color = colors.clay
                            )
                        }
                        Text(
                            text = IstTimeUtils.formatShortTime(stacked.time),
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp,
                            color = colors.mutedText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlotVaultCard(slot: ShortSlot, currentTime: Long) {
    val colors = AppTheme.colors

    val isDoing = slot.status == "DOING"
    val isDone = slot.status == "DONE"

    val startTime = slot.startTime ?: currentTime
    val elapsedMs = if (isDoing) (currentTime - startTime).coerceAtLeast(0L)
    else if (isDone && slot.doneTime != null && slot.startTime != null) slot.doneTime - slot.startTime
    else 0L

    val requiredMs = 3600000L // 1 hour
    val progress = (elapsedMs / requiredMs.toFloat()).coerceIn(0f, 1f)
    val remainingMs = (requiredMs - elapsedMs).coerceAtLeast(0L)

    val remainingMins = remainingMs / 60000
    val remainingSecs = (remainingMs % 60000) / 1000

    val statusText = when {
        isDone -> "COMPLETED LEGIT (1h+)"
        isDoing && elapsedMs >= requiredMs -> "Ready to confirm!"
        isDoing -> String.format(Locale.US, "%dm %02ds left", remainingMins, remainingSecs)
        else -> "Idle — Tap to start 1h timer"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(shadowColor = colors.ink, offsetX = 4.dp, offsetY = 4.dp, shape = WobblyGridShape)
            .background(colors.paper, WobblyGridShape)
            .border(2.dp, colors.ink, WobblyGridShape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "0${slot.id}",
                        fontFamily = InstrumentSerif,
                        fontSize = 24.sp,
                        color = colors.ink
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = slot.label,
                        fontFamily = Inter,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mutedText
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (slot.title.isBlank()) "Untitled Short" else slot.title,
                    fontFamily = Inter,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.ink
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusText,
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    color = if (isDoing && elapsedMs >= requiredMs) colors.sage else if (isDoing) colors.signalRed else colors.mutedText
                )
            }

            Box(
                modifier = Modifier.size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = if (isDone) colors.signalRed else colors.sage,
                    trackColor = colors.ink.copy(alpha = 0.1f),
                    strokeWidth = 4.dp
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontFamily = JetBrainsMono,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
            }
        }
    }
}

@Composable
fun DeepWorkSection(slots: List<ShortSlot>, currentTime: Long) {
    val colors = AppTheme.colors

    var totalFocusedMs = 0L
    val slotFocusedTimes = mutableMapOf<Int, Long>()

    slots.forEach { slot ->
        val focus = when {
            slot.status == "DONE" && slot.startTime != null && slot.doneTime != null -> slot.doneTime - slot.startTime
            slot.status == "DOING" && slot.startTime != null -> (currentTime - slot.startTime).coerceAtLeast(0L)
            else -> 0L
        }
        slotFocusedTimes[slot.id] = focus
        totalFocusedMs += focus
    }

    val totalHours = totalFocusedMs / 3600000f
    val progress = (totalHours / 3.0f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(shadowColor = colors.ink, offsetX = 4.dp, offsetY = 4.dp, shape = WobblyGridShape)
            .background(colors.paper, WobblyGridShape)
            .border(2.dp, colors.ink, WobblyGridShape)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DEEP WORK TODAY",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
                Text(
                    text = String.format(Locale.US, "%.1fh / 3h deep work", totalHours),
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = colors.signalRed,
                trackColor = colors.ink.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                slots.forEach { slot ->
                    val ms = slotFocusedTimes[slot.id] ?: 0L
                    val mins = ms / 60000
                    Text(
                        text = "Slot 0${slot.id}: ${mins}m",
                        fontFamily = JetBrainsMono,
                        fontSize = 9.sp,
                        color = colors.mutedText
                    )
                }
            }
        }
    }
}
