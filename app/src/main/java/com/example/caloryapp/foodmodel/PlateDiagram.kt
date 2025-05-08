package com.example.caloryapp.foodmodel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlateDiagram(
    categories: Map<FoodCategory, Float>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = minOf(canvasWidth, canvasHeight) / 2
            val center = androidx.compose.ui.geometry.Offset(canvasWidth / 2, canvasHeight / 2)

            // Draw plate outline
            drawCircle(
                color = Color.LightGray,
                radius = radius,
                center = center,
                style = Stroke(width = 2f)
            )

            // Draw food sections based on confidence
            var startAngle = 0f
            categories.forEach { (category, confidence) ->
                val sweepAngle = 360f * confidence
                val color = Color(android.graphics.Color.parseColor(category.colorHex))

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = center - androidx.compose.ui.geometry.Offset(radius, radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )

                startAngle += sweepAngle
            }
        }
    }
}