package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
fun ArchiveGrid(
    archiveDays: List<ArchiveDay>,
    onDayClick: (ArchiveDay) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {
        // Separator Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(colors.dividerDark)
            )
            Text(
                text = " ARCHIVE — 30D ",
                fontFamily = Inter,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mutedText,
                letterSpacing = 0.8.sp
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(colors.dividerDark)
            )
        }

        // 6 Columns Grid
        val displayList = if (archiveDays.size >= 30) archiveDays.take(30) else {
            val list = archiveDays.toMutableList()
            while (list.size < 30) {
                val nextNum = list.size + 1
                list.add(ArchiveDay(date = "DAY $nextNum", dayNumber = nextNum, count = 0, titles = "", realDurations = ""))
            }
            list
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .testTag("archive_grid")
        ) {
            items(displayList) { day ->
                ArchiveCell(day = day, onClick = { onDayClick(day) })
            }
        }
    }
}

@Composable
fun ArchiveCell(
    day: ArchiveDay,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .hardShadow(shadowColor = colors.ink, offsetX = 2.dp, offsetY = 2.dp, shape = WobblyGridShape)
            .background(colors.paper, WobblyGridShape)
            .border(1.5.dp, colors.ink, WobblyGridShape)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Top Date Number
        Text(
            text = day.dayNumber.toString(),
            fontFamily = JetBrainsMono,
            fontSize = 9.sp,
            color = colors.ink,
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Center Dots based on count
        when (day.count) {
            1 -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 2.dp)
                        .size(6.dp)
                        .background(colors.clay, CircleShape)
                )
            }
            2 -> {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(colors.clay, CircleShape))
                    Box(modifier = Modifier.size(6.dp).background(colors.clay, CircleShape))
                }
            }
            3 -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(8.dp)
                        .background(colors.signalRed, CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "3/3 Complete",
                    tint = colors.signalRed,
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                )
            }
            else -> {
                // Blank 0/3
            }
        }
    }
}
