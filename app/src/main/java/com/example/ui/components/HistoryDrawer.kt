package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArchiveDay
import com.example.ui.theme.AppTheme
import com.example.ui.theme.InstrumentSerif
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono

@Composable
fun HistoryDrawer(
    day: ArchiveDay,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val titlesList = if (day.titles.isBlank()) emptyList() else day.titles.split("|")

    val drawerInk = colors.ink

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    color = drawerInk,
                    topLeft = androidx.compose.ui.geometry.Offset(0f, -12f),
                    size = androidx.compose.ui.geometry.Size(size.width, 12f)
                )
            }
            .background(
                colors.paper,
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .border(
                2.5.dp,
                colors.ink,
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .paperGrainOverlay(0.04f)
            .padding(16.dp)
            .testTag("history_drawer")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(bottom = 8.dp)
            ) {
                val path = Path()
                val steps = 24
                val stepW = size.width / steps
                path.moveTo(0f, size.height)
                for (i in 0..steps) {
                    val y = if (i % 2 == 0) 0f else size.height
                    path.lineTo(i * stepW, y)
                }
                drawPath(path = path, color = drawerInk, style = Stroke(width = 1.5.dp.toPx()))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${day.date} — DAY ${day.dayNumber}",
                    fontFamily = JetBrainsMono,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.ink
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(1.5.dp, colors.ink, CircleShape)
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.ink,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val labels = listOf("MORNING", "AFTERNOON", "EVENING")
            for (i in 0..2) {
                val numStr = "0${i + 1}"
                val title = if (i < titlesList.size && titlesList[i].isNotBlank()) titlesList[i] else "—"
                val isCompleted = i < day.count

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = numStr,
                        fontFamily = InstrumentSerif,
                        fontSize = 18.sp,
                        color = colors.mutedText
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = labels[i],
                            fontFamily = Inter,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.mutedText
                        )
                        Text(
                            text = title,
                            fontFamily = Inter,
                            fontSize = 13.sp,
                            color = colors.ink
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (isCompleted) colors.signalRed else colors.sage.copy(alpha = 0.3f),
                                CircleShape
                            )
                            .border(1.dp, colors.ink, CircleShape)
                    )
                }
            }
        }
    }
}
