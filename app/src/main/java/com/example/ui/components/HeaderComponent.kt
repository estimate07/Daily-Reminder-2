package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Ink
import com.example.ui.theme.InstrumentSerif
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.MutedText
import com.example.ui.theme.Paper
import com.example.ui.theme.Sage
import com.example.ui.theme.SignalRed
import com.example.util.IstTimeUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeaderComponent(
    streak: Int,
    freezeShields: Int,
    modifier: Modifier = Modifier
) {
    var countdownMillis by remember { mutableLongStateOf(IstTimeUtils.getMillisTillNext1AmIst()) }

    LaunchedEffect(Unit) {
        while (true) {
            countdownMillis = IstTimeUtils.getMillisTillNext1AmIst()
            delay(1000)
        }
    }

    val isUrgent = countdownMillis < 600000L // < 10 min left

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT: Streak (Feature 3)
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.testTag("streak_header")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = streak.toString(),
                    fontFamily = InstrumentSerif,
                    fontSize = 40.sp,
                    color = Paper,
                    lineHeight = 40.sp
                )
                if (streak > 7) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .alpha(pulseAlpha)
                            .background(SignalRed, CircleShape)
                    )
                }
            }
            // Tally marks (max 14 displayed in max 60dp wide flow row)
            FlowRow(
                modifier = Modifier
                    .width(60.dp)
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val displayTally = streak.coerceAtMost(14)
                repeat(displayTally) {
                    Box(
                        modifier = Modifier
                            .width(1.5.dp)
                            .height(10.dp)
                            .background(Paper)
                    )
                }
            }
            Text(
                text = "DAY RUN",
                fontFamily = Inter,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = MutedText,
                letterSpacing = 0.5.sp
            )
        }

        // CENTER: Weekly (Feature 12) - 21 dots in 2 rows (11 + 10)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.testTag("weekly_header")
        ) {
            val filledCount = streak.coerceAtMost(21)
            // Row 1: 11 dots
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                for (i in 1..11) {
                    val isFilled = i <= filledCount
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(if (isFilled) Paper else Color.Transparent, CircleShape)
                            .border(1.2.dp, Paper, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            // Row 2: 10 dots
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                for (i in 12..21) {
                    val isFilled = i <= filledCount
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(if (isFilled) Paper else Color.Transparent, CircleShape)
                            .border(1.2.dp, Paper, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$filledCount/21 ",
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    color = Paper
                )
                Text(
                    text = "WEEK",
                    fontFamily = Inter,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedText,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // RIGHT: Freeze Shield + Countdown
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.testTag("shield_countdown_header")
        ) {
            // Top Freeze Shield (Feature 10)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.rotate(3f)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 28.dp)
                        .background(Sage, ShieldShape)
                        .border(1.5.dp, Ink, ShieldShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = freezeShields.toString(),
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SHIELD",
                    fontFamily = Inter,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedText,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bottom Countdown (Feature 8)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = IstTimeUtils.formatCountdown(countdownMillis),
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    color = if (isUrgent) SignalRed.copy(alpha = pulseAlpha) else Paper
                )
                Text(
                    text = "TILL 1AM IST",
                    fontFamily = Inter,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedText,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// Custom Shield Shape for the Freeze Shield
val ShieldShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    moveTo(w * 0.5f, 0f)
    lineTo(w, h * 0.2f)
    lineTo(w * 0.85f, h * 0.75f)
    lineTo(w * 0.5f, h)
    lineTo(w * 0.15f, h * 0.75f)
    lineTo(0f, h * 0.2f)
    close()
}
