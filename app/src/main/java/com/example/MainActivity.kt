package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RovioStableApp()
        }
    }
}

private enum class SlotStatus(val label: String) {
    Todo("TODO"),
    Doing("DOING"),
    Done("DONE")
}

private data class ReminderSlot(
    val id: Int,
    val title: String,
    val timeLabel: String,
    val status: SlotStatus = SlotStatus.Todo
)

@Composable
private fun RovioStableApp() {
    val slots = remember {
        mutableStateListOf(
            ReminderSlot(1, "Morning short", "Start before 10:00 AM"),
            ReminderSlot(2, "Afternoon short", "Start before 3:00 PM"),
            ReminderSlot(3, "Evening short", "Start before 8:00 PM")
        )
    }
    val doneCount = slots.count { it.status == SlotStatus.Done }
    val progress = doneCount / slots.size.toFloat()

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("main_screen"),
            color = Color(0xFF08111F)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0E1B33),
                                Color(0xFF101827),
                                Color(0xFF050816)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Header(doneCount = doneCount)
                    ProgressPanel(progress = progress, doneCount = doneCount, total = slots.size)
                    slots.forEachIndexed { index, slot ->
                        ReminderCard(
                            slot = slot,
                            onAdvance = {
                                slots[index] = slot.copy(status = slot.status.next())
                            }
                        )
                    }
                    ResetPanel(
                        onReset = {
                            slots.indices.forEach { index ->
                                slots[index] = slots[index].copy(status = SlotStatus.Todo)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(doneCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "ROVIO DAILY 3",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = if (doneCount == 3) {
                "All shorts locked in. Clean finish."
            } else {
                "Simple crash-safe tracker for three daily shorts."
            },
            color = Color(0xFFB8C7E0),
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun ProgressPanel(progress: Float, doneCount: Int, total: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16243A)),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today progress",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$doneCount / $total",
                    color = Color(0xFF7DD3FC),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Color(0xFF22C55E),
                trackColor = Color(0xFF263852)
            )
        }
    }
}

@Composable
private fun ReminderCard(slot: ReminderSlot, onAdvance: () -> Unit) {
    val accent = when (slot.status) {
        SlotStatus.Todo -> Color(0xFFF97316)
        SlotStatus.Doing -> Color(0xFF38BDF8)
        SlotStatus.Done -> Color(0xFF22C55E)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot.id.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                    Column {
                        Text(
                            text = slot.title,
                            color = Color(0xFF0F172A),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = slot.timeLabel,
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                }
                StatusBadge(status = slot.status, color = accent)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAdvance,
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = slot.status.actionLabel(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: SlotStatus, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = status.label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun ResetPanel(onReset: () -> Unit) {
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onReset,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = "Reset today",
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun SlotStatus.next(): SlotStatus = when (this) {
    SlotStatus.Todo -> SlotStatus.Doing
    SlotStatus.Doing -> SlotStatus.Done
    SlotStatus.Done -> SlotStatus.Todo
}

private fun SlotStatus.actionLabel(): String = when (this) {
    SlotStatus.Todo -> "Start this short"
    SlotStatus.Doing -> "Mark completed"
    SlotStatus.Done -> "Reopen slot"
}
