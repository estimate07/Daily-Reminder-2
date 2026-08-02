package com.example.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import kotlin.random.Random

fun Modifier.paperGrainOverlay(alpha: Float = 0.04f): Modifier = this.drawWithCache {
    val width = size.width.toInt()
    val height = size.height.toInt()
    val numDots = if (width > 0 && height > 0) (width * height / 120).coerceIn(20, 2000) else 0
    val random = Random(42)
    val points = ArrayList<androidx.compose.ui.geometry.Offset>(numDots)
    for (i in 0 until numDots) {
        val x = random.nextFloat() * size.width
        val y = random.nextFloat() * size.height
        points.add(androidx.compose.ui.geometry.Offset(x, y))
    }

    onDrawWithContent {
        drawContent()
        if (points.isNotEmpty()) {
            drawPoints(
                points = points,
                pointMode = PointMode.Points,
                color = Color.Black.copy(alpha = alpha),
                strokeWidth = 1.2f
            )
        }
    }
}
