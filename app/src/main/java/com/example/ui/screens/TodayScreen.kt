package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.ArchiveDay
import com.example.data.ShortSlot
import com.example.ui.components.ArchiveGrid
import com.example.ui.components.HeaderComponent
import com.example.ui.components.MiddleStoryCards
import com.example.ui.theme.Desk
import com.example.ui.theme.DividerDark
import com.example.ui.theme.Paper

@Composable
fun TodayScreen(
    streak: Int,
    freezeShields: Int,
    slots: List<ShortSlot>,
    archiveDays: List<ArchiveDay>,
    showHitEffect: Boolean,
    onSlotClick: (ShortSlot) -> Unit,
    onTitleClick: (ShortSlot) -> Unit,
    onHoldDoneConfirmed: (Int) -> Unit,
    onArchiveDayClick: (ArchiveDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Desk),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("today_screen"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header 72dp
            HeaderComponent(
                streak = streak,
                freezeShields = freezeShields
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Middle 3 Cards
            MiddleStoryCards(
                slots = slots,
                onSlotClick = onSlotClick,
                onTitleClick = onTitleClick,
                onHoldDoneConfirmed = onHoldDoneConfirmed,
                showHitEffect = showHitEffect
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Weekly Progress Line (2dp height)
            val filledFraction = (streak.coerceAtMost(21) / 21f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(DividerDark)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(filledFraction)
                        .height(2.dp)
                        .background(Paper)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Archive Grid (Feature 6)
            ArchiveGrid(
                archiveDays = archiveDays,
                onDayClick = onArchiveDayClick
            )
        }
    }
}
