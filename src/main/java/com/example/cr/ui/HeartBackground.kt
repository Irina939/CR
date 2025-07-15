package com.example.cr.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HeartBackground(
    modifier: Modifier = Modifier,
    heartColor: Color = Color(0xFFFC8899),
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        val heartCount = 12
        val screenWidth = 1080f // px, для адаптации можно вычислять через LocalDensity
        val screenHeight = 1920f
        var hearts by remember { mutableStateOf(List(heartCount) { randomHeart(screenWidth, screenHeight) }) }

        // Анимация падения
        LaunchedEffect(Unit) {
            while (true) {
                hearts = hearts.map { heart ->
                    val newY = heart.y + heart.speed
                    if (newY > screenHeight) randomHeart(screenWidth, 0f) else heart.copy(y = newY)
                }
                delay(16L) // ~60 FPS
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            hearts.forEach { heart ->
                val path = Path().apply {
                    addHeart(heart.x, heart.y, heart.size)
                }
                drawPath(path, color = heartColor.copy(alpha = heart.alpha), style = Fill)
            }
        }
        content()
    }
}

data class Heart(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float
)

fun randomHeart(screenWidth: Float, y: Float): Heart {
    val size = Random.nextFloat() * 40f + 40f // 40-80 px
    val x = Random.nextFloat() * (screenWidth - size) + size / 2
    val speed = Random.nextFloat() * 3f + 2f // 2-5 px/frame
    val alpha = Random.nextFloat() * 0.5f + 0.5f // 0.5-1.0
    return Heart(x, y, size, speed, alpha)
}

fun Path.addHeart(centerX: Float, centerY: Float, size: Float) {
    // Нижний кончик
    moveTo(centerX, centerY + size / 4)
    // Левая половина
    cubicTo(
        centerX - size, centerY - size / 2,
        centerX - size, centerY + size,
        centerX, centerY + size
    )
    // Правая половина
    cubicTo(
        centerX + size, centerY + size,
        centerX + size, centerY - size / 2,
        centerX, centerY + size / 4
    )
    close()
} 