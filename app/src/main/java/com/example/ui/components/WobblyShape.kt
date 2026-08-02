package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Ink

val Card01Shape = RoundedCornerShape(
    topStart = 14.dp,
    topEnd = 4.dp,
    bottomEnd = 12.dp,
    bottomStart = 4.dp
)

val Card02Shape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 14.dp,
    bottomEnd = 4.dp,
    bottomStart = 12.dp
)

val Card03Shape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomEnd = 14.dp,
    bottomStart = 4.dp
)

val WobblyGridShape = RoundedCornerShape(
    topStart = 6.dp,
    topEnd = 4.dp,
    bottomEnd = 7.dp,
    bottomStart = 5.dp
)

fun Modifier.hardShadow(
    shadowColor: Color = Ink,
    offsetX: Dp = 5.dp,
    offsetY: Dp = 5.dp,
    shape: Shape = Card01Shape
): Modifier = this.drawBehind {
    val pxX = offsetX.toPx()
    val pxY = offsetY.toPx()

    drawContext.canvas.save()
    drawContext.canvas.translate(pxX, pxY)
    val outline = shape.createOutline(size, layoutDirection, this)
    when (outline) {
        is Outline.Rectangle -> drawRect(shadowColor, size = outline.rect.size)
        is Outline.Rounded -> drawRoundRect(
            color = shadowColor,
            topLeft = Offset.Zero,
            size = outline.bounds.size,
            cornerRadius = outline.roundRect.topLeftCornerRadius
        )
        is Outline.Generic -> drawPath(outline.path, shadowColor)
    }
    drawContext.canvas.restore()
}
