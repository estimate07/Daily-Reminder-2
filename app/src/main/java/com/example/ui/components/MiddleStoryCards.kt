package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShortSlot
import com.example.ui.theme.AppTheme
import com.example.ui.theme.InstrumentSerif
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono
import com.example.util.IstTimeUtils
import kotlinx.coroutines.delay

@Composable
fun MiddleStoryCards(
    slots: List<ShortSlot>,
    onSlotClick: (ShortSlot) -> Unit,
    onTitleClick: (ShortSlot) -> Unit,
    onHoldDoneConfirmed: (Int) -> Unit,
    showHitEffect: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    val overallStatus = when {
        slots.all { it.status == "DONE" } -> "DONE"
        slots.any { it.status == "DOING" } -> "DOING"
        else -> "TODO"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Text(
            text = "TODAY'S EDITION — 3 STORIES",
            fontFamily = Inter,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = colors.mutedText,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Cards Stack
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            slots.forEachIndexed { index, slot ->
                val cardShape = when (index) {
                    0 -> Card01Shape
                    1 -> Card02Shape
                    else -> Card03Shape
                }
                val cardRotation = when (index) {
                    0 -> -1.2f
                    1 -> 0.8f
                    else -> -0.6f
                }

                // Shake animation on hit effect
                val shakeOffset = if (showHitEffect) {
                    val transition = rememberInfiniteTransition(label = "shake")
                    val offset by transition.animateFloat(
                        initialValue = -2f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(60, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "shakeOffset"
                    )
                    offset
                } else 0f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = shakeOffset.dp)
                        .rotate(cardRotation)
                ) {
                    // Cameo Stick Figure on middle card top edge
                    if (index == 1) {
                        StickFigureCameo(
                            status = overallStatus,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-24).dp, y = (-18).dp)
                        )
                    }

                    SingleStoryCard(
                        slot = slot,
                        index = index + 1,
                        cardShape = cardShape,
                        onStatusClick = { onSlotClick(slot) },
                        onTitleClick = { onTitleClick(slot) },
                        onHoldDoneConfirmed = { onHoldDoneConfirmed(slot.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SingleStoryCard(
    slot: ShortSlot,
    index: Int,
    cardShape: androidx.compose.ui.graphics.Shape,
    onStatusClick: () -> Unit,
    onTitleClick: () -> Unit,
    onHoldDoneConfirmed: () -> Unit
) {
    val colors = AppTheme.colors

    val isDone = slot.status == "DONE"
    val isDoing = slot.status == "DOING"

    val borderColor = if (isDone) colors.signalRed else colors.ink
    val shadowColor = if (isDone) colors.signalRed else colors.ink
    val bgColor = if (isDone) colors.paper.copy(alpha = 0.96f) else colors.paper

    var holdProgress by remember { mutableFloatStateOf(0f) }
    var isHolding by remember { mutableStateOf(false) }

    LaunchedEffect(isHolding) {
        if (isHolding) {
            val startTime = System.currentTimeMillis()
            while (isHolding) {
                val elapsed = System.currentTimeMillis() - startTime
                holdProgress = (elapsed / 3000f).coerceIn(0f, 1f)
                if (holdProgress >= 1f) {
                    onHoldDoneConfirmed()
                    isHolding = false
                    holdProgress = 0f
                    break
                }
                delay(16)
            }
        } else {
            holdProgress = 0f
        }
    }

    val numStr = String.format("%02d", index)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .hardShadow(shadowColor = shadowColor, offsetX = 5.dp, offsetY = 5.dp, shape = cardShape)
            .background(bgColor, cardShape)
            .border(2.dp, borderColor, cardShape)
            .paperGrainOverlay(0.03f)
            .padding(12.dp)
            .testTag("story_card_$index")
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT 60dp: Number + Label
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = numStr,
                    fontFamily = InstrumentSerif,
                    fontSize = 32.sp,
                    color = if (slot.status == "TODO") colors.ink.copy(alpha = 0.2f) else colors.ink,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = slot.label,
                    fontFamily = Inter,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText,
                    letterSpacing = 0.5.sp
                )
            }

            // CENTER weight 1f: Title
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTitleClick() }
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (slot.title.isBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Add title...",
                            fontFamily = Inter,
                            fontStyle = FontStyle.Italic,
                            fontSize = 13.sp,
                            color = colors.mutedText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit title",
                            tint = colors.mutedText,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                } else {
                    Text(
                        text = slot.title,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colors.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // RIGHT 64dp: Status button
            Column(
                modifier = Modifier.width(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .pointerInput(slot.status) {
                            detectTapGestures(
                                onTap = {
                                    if (isDoing) {
                                        onStatusClick()
                                    } else {
                                        onStatusClick()
                                    }
                                },
                                onPress = {
                                    if (isDoing) {
                                        isHolding = true
                                        tryAwaitRelease()
                                        isHolding = false
                                        holdProgress = 0f
                                    }
                                }
                            )
                        }
                        .background(
                            when {
                                isDone -> colors.signalRed
                                isDoing -> colors.sage
                                else -> Color.Transparent
                            },
                            CircleShape
                        )
                        .border(2.dp, colors.ink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDoing && holdProgress > 0f) {
                        val strokeColor = colors.signalRed
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = strokeColor,
                                startAngle = -90f,
                                sweepAngle = 360f * holdProgress,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }

                    when {
                        isDone -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "DONE",
                                tint = colors.paper,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        isDoing -> {
                            val infiniteTransition = rememberInfiniteTransition(label = "dotPulse")
                            val dotAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.4f,
                                targetValue = 1.0f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(600, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "dotAlpha"
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .alpha(dotAlpha)
                                    .background(colors.paper, CircleShape)
                            )
                        }
                        else -> {
                            // TODO outline circle empty
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = slot.status,
                    fontFamily = JetBrainsMono,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Timestamp when DONE
        if (isDone && slot.doneTime != null) {
            Text(
                text = "done ${IstTimeUtils.formatShortTime(slot.doneTime)}",
                fontFamily = JetBrainsMono,
                fontSize = 8.sp,
                color = colors.mutedText,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 2.dp)
            )
        }
    }
}

@Composable
fun StickFigureCameo(
    status: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val translateY = if (status == "DONE") (-6).dp else 0.dp

    Box(
        modifier = modifier
            .offset(y = translateY)
            .size(24.dp)
    ) {
        val figureInk = colors.ink
        val figureRed = colors.signalRed
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 3)
            val headRadius = 6.dp.toPx()

            // Head
            drawCircle(
                color = Color.White,
                radius = headRadius,
                center = center
            )
            drawCircle(
                color = figureInk,
                radius = headRadius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Red dot accent
            drawCircle(
                color = figureRed,
                radius = 1.5.dp.toPx(),
                center = Offset(center.x + 3.dp.toPx(), center.y - 2.dp.toPx())
            )

            // Body line
            val bodyStart = Offset(center.x, center.y + headRadius)
            val bodyEnd = Offset(center.x, size.height - 2.dp.toPx())
            drawLine(
                color = figureInk,
                start = bodyStart,
                end = bodyEnd,
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}
