package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShortSlot
import com.example.ui.theme.AppTheme
import com.example.ui.theme.InstrumentSerif
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono
import com.example.util.IstTimeUtils
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailySummaryTab(
    slots: List<ShortSlot>,
    streak: Int,
    onSlotClick: (ShortSlot) -> Unit,
    onTitleClick: (ShortSlot) -> Unit,
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

    val doneCount = slots.count { it.status == "DONE" }
    val doingCount = slots.count { it.status == "DOING" }
    val todoCount = slots.count { it.status == "TODO" }
    val totalSlots = 3

    val completionRatio = doneCount / totalSlots.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = completionRatio,
        animationSpec = tween(1000),
        label = "completionProgress"
    )

    val todayFormatted = remember {
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
        sdf.format(Date())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("daily_summary_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Title & Date
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DAILY SUMMARY",
                    fontFamily = Inter,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.paper,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = todayFormatted.uppercase(Locale.US),
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    color = colors.mutedText
                )
            }
        }

        // Main Completion Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(shadowColor = colors.ink, offsetX = 5.dp, offsetY = 5.dp, shape = WobblyGridShape)
                    .background(colors.paper, WobblyGridShape)
                    .border(2.5.dp, colors.ink, WobblyGridShape)
                    .padding(16.dp)
                    .testTag("summary_hero_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (doneCount) {
                                3 -> "ALL 3 SHORTS DONE!"
                                2 -> "2 OF 3 COMPLETED"
                                1 -> "1 OF 3 COMPLETED"
                                else -> "NO SHORTS DONE YET"
                            },
                            fontFamily = InstrumentSerif,
                            fontSize = 24.sp,
                            color = colors.ink,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = when (doneCount) {
                                3 -> "Perfect daily output. All 3 YouTube shorts published to Room DB."
                                2 -> "Great momentum. 1 short left for full daily streak."
                                1 -> "Off to a start. Keep focused on the remaining 2 shorts."
                                else -> "Start your first short timer to lock in your daily work."
                            },
                            fontFamily = Inter,
                            fontSize = 11.sp,
                            color = colors.mutedText,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(if (doneCount == 3) colors.sage else colors.clay, RoundedCornerShape(4.dp))
                                    .border(1.dp, colors.ink, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "$streak DAY STREAK",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.ink
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${(completionRatio * 100).toInt()}% COMPLETE",
                                fontFamily = JetBrainsMono,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.signalRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Circular Completion Ring
                    Box(
                        modifier = Modifier.size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val trackColor = colors.ink.copy(alpha = 0.12f)
                        val progressColor = if (doneCount == 3) colors.signalRed else colors.sage
                        val inkColor = colors.ink

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = trackColor,
                                style = Stroke(width = 7.dp.toPx())
                            )
                            drawArc(
                                color = progressColor,
                                startAngle = -90f,
                                sweepAngle = 360f * animatedProgress,
                                useCenter = false,
                                style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$doneCount/3",
                                fontFamily = InstrumentSerif,
                                fontSize = 22.sp,
                                color = inkColor
                            )
                            Text(
                                text = "SHORTS",
                                fontFamily = Inter,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.mutedText
                            )
                        }
                    }
                }
            }
        }

        // Status Pills Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusMetricPill(
                    label = "DONE",
                    count = doneCount,
                    total = 3,
                    color = colors.signalRed,
                    modifier = Modifier.weight(1f)
                )
                StatusMetricPill(
                    label = "DOING",
                    count = doingCount,
                    total = 3,
                    color = colors.sage,
                    modifier = Modifier.weight(1f)
                )
                StatusMetricPill(
                    label = "TODO",
                    count = todoCount,
                    total = 3,
                    color = colors.paper,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section Title: YOUTUBE SHORTS BREAKDOWN
        item {
            Text(
                text = "TODAY'S 3 SHORTS (ROOM DB PERSISTED)",
                fontFamily = Inter,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mutedText,
                letterSpacing = 0.8.sp
            )
        }

        // 3 Shorts Cards List
        items(slots) { slot ->
            ShortSummaryCard(
                slot = slot,
                currentTime = currentTime,
                onSlotClick = { onSlotClick(slot) },
                onTitleClick = { onTitleClick(slot) }
            )
        }

        // Room DB Health & Deep Work Insights Footer
        item {
            val totalFocusedMs = slots.sumOf { slot ->
                when {
                    slot.status == "DONE" && slot.startTime != null && slot.doneTime != null -> slot.doneTime - slot.startTime
                    slot.status == "DOING" && slot.startTime != null -> (currentTime - slot.startTime).coerceAtLeast(0L)
                    else -> 0L
                }
            }
            val totalMins = totalFocusedMs / 60000

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.paper, RoundedCornerShape(8.dp))
                    .border(1.5.dp, colors.ink, RoundedCornerShape(8.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ROOM DATABASE STATUS",
                            fontFamily = Inter,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink
                        )
                        Box(
                            modifier = Modifier
                                .background(colors.sage.copy(alpha = 0.25f), CircleShape)
                                .border(1.dp, colors.ink, CircleShape)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PERSISTED",
                                fontFamily = JetBrainsMono,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.ink
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Total Deep Work Today: ${totalMins} minutes across 3 slots.",
                        fontFamily = JetBrainsMono,
                        fontSize = 10.sp,
                        color = colors.mutedText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val progressFraction = (totalMins / 180f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = colors.signalRed,
                        trackColor = colors.ink.copy(alpha = 0.12f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusMetricPill(
    label: String,
    count: Int,
    total: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier
            .background(colors.paper, RoundedCornerShape(6.dp))
            .border(1.5.dp, colors.ink, RoundedCornerShape(6.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
                    .border(1.dp, colors.ink, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label: $count/$total",
                fontFamily = JetBrainsMono,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colors.ink
            )
        }
    }
}

@Composable
fun ShortSummaryCard(
    slot: ShortSlot,
    currentTime: Long,
    onSlotClick: () -> Unit,
    onTitleClick: () -> Unit
) {
    val colors = AppTheme.colors

    val isDone = slot.status == "DONE"
    val isDoing = slot.status == "DOING"

    val startTime = slot.startTime ?: currentTime
    val elapsedMs = if (isDoing) (currentTime - startTime).coerceAtLeast(0L)
    else if (isDone && slot.doneTime != null && slot.startTime != null) slot.doneTime - slot.startTime
    else 0L

    val elapsedMins = elapsedMs / 60000

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(shadowColor = colors.ink, offsetX = 4.dp, offsetY = 4.dp, shape = WobblyGridShape)
            .background(colors.paper, WobblyGridShape)
            .border(2.dp, if (isDone) colors.signalRed else colors.ink, WobblyGridShape)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "0${slot.id}",
                        fontFamily = InstrumentSerif,
                        fontSize = 28.sp,
                        color = colors.ink
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = slot.label, // MORNING, AFTERNOON, EVENING
                            fontFamily = Inter,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.mutedText,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "SLOT 0${slot.id}",
                            fontFamily = JetBrainsMono,
                            fontSize = 7.sp,
                            color = colors.mutedText
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clickable { onSlotClick() }
                        .background(
                            when {
                                isDone -> colors.signalRed
                                isDoing -> colors.sage
                                else -> colors.ink.copy(alpha = 0.1f)
                            },
                            RoundedCornerShape(4.dp)
                        )
                        .border(1.dp, colors.ink, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when {
                                isDone -> Icons.Default.Check
                                isDoing -> Icons.Default.PlayArrow
                                else -> Icons.Default.Schedule
                            },
                            contentDescription = slot.status,
                            tint = if (isDone) colors.paper else colors.ink,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = slot.status,
                            fontFamily = JetBrainsMono,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDone) colors.paper else colors.ink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTitleClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (slot.title.isBlank()) "Tap to edit short title..." else slot.title,
                    fontFamily = Inter,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (slot.title.isBlank()) colors.mutedText else colors.ink,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit title",
                    tint = colors.mutedText,
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val timeInfoText = when {
                    isDone && slot.doneTime != null -> "Completed at ${IstTimeUtils.formatShortTime(slot.doneTime)}"
                    isDoing && slot.startTime != null -> "Started at ${IstTimeUtils.formatShortTime(slot.startTime)}"
                    else -> "Not started yet"
                }

                Text(
                    text = timeInfoText,
                    fontFamily = JetBrainsMono,
                    fontSize = 9.sp,
                    color = colors.mutedText
                )

                Text(
                    text = "${elapsedMins}m logged",
                    fontFamily = JetBrainsMono,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) colors.signalRed else colors.ink
                )
            }
        }
    }
}
