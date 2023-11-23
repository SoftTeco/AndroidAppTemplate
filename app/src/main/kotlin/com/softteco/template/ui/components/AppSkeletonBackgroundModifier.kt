package com.softteco.template.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode

private const val END_OFFSET_DELTA = 100f

fun Modifier.skeletonBackground(shape: Shape = RectangleShape): Modifier {
    return composed {
        val transition = rememberInfiniteTransition(label = "skeletonTransition")

        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 500f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
                RepeatMode.Restart
            ),
            label = "skeletonAnimation",
        )
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.run { LightGray.copy(alpha = 0.4f) },
        )
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation, translateAnimation),
            end = Offset(
                translateAnimation + END_OFFSET_DELTA,
                translateAnimation + END_OFFSET_DELTA
            ),
            tileMode = TileMode.Mirror,
        )
        return@composed this.then(background(brush, shape))
    }
}
