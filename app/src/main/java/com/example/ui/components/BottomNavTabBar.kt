package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Desk
import com.example.ui.theme.Ink
import com.example.ui.theme.Inter
import com.example.ui.theme.MutedText
import com.example.ui.theme.Paper
import com.example.ui.theme.SignalRed

@Composable
fun BottomNavTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabTitles = listOf("TODAY", "TIME VAULT", "AUDIT")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Desk)
            .navigationBarsPadding()
            .padding(12.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Ink, RoundedCornerShape(8.dp))
                .border(1.5.dp, Paper, RoundedCornerShape(8.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabTitles.forEachIndexed { index, title ->
                val isSelected = selectedTab == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .background(
                            if (isSelected) Paper else Ink,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onTabSelected(index) }
                        .testTag("tab_button_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontFamily = Inter,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Ink else Paper.copy(alpha = 0.6f),
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
