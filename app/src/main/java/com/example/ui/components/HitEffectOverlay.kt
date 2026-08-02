package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import kotlin.random.Random

@Composable
fun HitEffectOverlay(
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val random = remember { Random(1234) }
        val particles = remember {
            List(16) { i ->
                val isPaper = i < 10
                val targetX = random.nextInt(-80, 80).toFloat()
                val targetY = random.nextInt(-20, 120).toFloat()
                val targetRot = random.nextInt(-15, 15).toFloat()
                val stagger = i * 20L
                ParticleSpec(isPaper, targetX, targetY, targetRot, stagger)
            }
        }

        particles.forEach { spec ->
            val animX = remember { Animatable(0f) }
            val animY = remember { Animatable(0f) }
            val animRot = remember { Animatable(0f) }

            LaunchedEffect(spec) {
                kotlinx.coroutines.delay(spec.stagger)
                animX.animateTo(spec.targetX, animationSpec = tween(900, easing = LinearEasing))
            }
            LaunchedEffect(spec) {
                kotlinx.coroutines.delay(spec.stagger)
                animY.animateTo(spec.targetY, animationSpec = tween(900, easing = LinearEasing))
            }
            LaunchedEffect(spec) {
                kotlinx.coroutines.delay(spec.stagger)
                animRot.animateTo(spec.targetRot, animationSpec = tween(900, easing = LinearEasing))
            }

            if (spec.isPaper) {
                Box(
                    modifier = Modifier
                        .offset(x = animX.value.dp, y = animY.value.dp)
                        .rotate(animRot.value)
                        .size(6.dp)
                        .background(colors.paper, RectangleShape)
                        .border(1.dp, colors.ink, RectangleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .offset(x = animX.value.dp, y = animY.value.dp)
                        .rotate(animRot.value)
                        .size(4.dp)
                        .background(colors.signalRed, RectangleShape)
                )
            }
        }

        repeat(8) { i ->
            val angle = i * 45f
            val rad = Math.toRadians(angle.toDouble())
            val targetX = (Math.cos(rad) * 60).toFloat()
            val targetY = (Math.sin(rad) * 60).toFloat()

            val animR = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                animR.animateTo(1f, animationSpec = tween(800))
            }

            Box(
                modifier = Modifier
                    .offset(x = (targetX * animR.value).dp, y = (targetY * animR.value).dp)
                    .size(3.dp)
                    .background(colors.ink, CircleShape)
            )
        }
    }
}

private data class ParticleSpec(
    val isPaper: Boolean,
    val targetX: Float,
    val targetY: Float,
    val targetRot: Float,
    val stagger: Long
)
