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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArchiveDay
import com.example.ui.theme.AppTheme
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono

@Composable
fun AuditTab(
    archiveDays: List<ArchiveDay>,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val weekDays = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("audit_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "WEEKLY WORK AUDIT & GRADES",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colors.paper,
                letterSpacing = 0.8.sp
            )
        }

        // 7 Days Audit List
        items(weekDays.size) { index ->
            val dayName = weekDays[index]
            val archiveItem = if (index < archiveDays.size) archiveDays[index] else null

            val durations = archiveItem?.realDurations?.split("|") ?: listOf("3720000", "3850000", "4100000")

            val d1 = durations.getOrNull(0)?.toLongOrNull() ?: 3720000L // ~62m
            val d2 = durations.getOrNull(1)?.toLongOrNull() ?: 3850000L // ~64m
            val d3 = durations.getOrNull(2)?.toLongOrNull() ?: 4100000L // ~68m

            val isLegit1 = d1 >= 3600000L
            val isLegit2 = d2 >= 3600000L
            val isLegit3 = d3 >= 3600000L

            val legitCount = listOf(isLegit1, isLegit2, isLegit3).count { it }
            val grade = when (legitCount) {
                3 -> "Grade A"
                2 -> "Grade B"
                else -> "Grade C"
            }
            val gradeColor = when (legitCount) {
                3 -> colors.sage
                2 -> colors.clay
                else -> colors.signalRed
            }

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
                            text = "$dayName AUDIT",
                            fontFamily = Inter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.ink
                        )
                        Box(
                            modifier = Modifier
                                .background(gradeColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .border(1.dp, colors.ink, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = grade,
                                fontFamily = JetBrainsMono,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.ink
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3 Shorts Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AuditSlotPill(slotNum = 1, durationMs = d1, isLegit = isLegit1)
                        AuditSlotPill(slotNum = 2, durationMs = d2, isLegit = isLegit2)
                        AuditSlotPill(slotNum = 3, durationMs = d3, isLegit = isLegit3)
                    }
                }
            }
        }
    }
}

@Composable
fun AuditSlotPill(slotNum: Int, durationMs: Long, isLegit: Boolean) {
    val colors = AppTheme.colors

    val mins = durationMs / 60000
    val hours = mins / 60
    val remMins = mins % 60

    val timeStr = if (hours > 0) "${hours}h${remMins}m" else "${mins}m"
    val tag = if (isLegit) "legit" else "rushed"
    val tagColor = if (isLegit) colors.sage else colors.clay

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(tagColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "0$slotNum $timeStr $tag",
            fontFamily = JetBrainsMono,
            fontSize = 9.sp,
            color = colors.ink
        )
    }
}
