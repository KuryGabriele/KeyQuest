package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview

data class DisplayNote(
    val pitch: String,
    val positionX: Float,
    val positionY: Float
)


@Preview
@Composable
fun MusicSheet (
    modifier: Modifier = Modifier,
    notes: List<DisplayNote> = listOf(DisplayNote(
        pitch = "C4",
        positionX = 100f,
        positionY = 300f
    )),
) {
    val lineSpacing = 50f
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    Canvas(
        modifier= Modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        for(i in 1..5) {
            drawLine(
                color = tertiaryColor,
                start = Offset(0f, i * lineSpacing),
                end = Offset(width, i * lineSpacing),
                strokeWidth = 4f
            )
        }

        for(note in notes) {
            drawCircle(
                color = primaryColor,
                radius = 20f,
                center = Offset(note.positionX, note.positionY)
            )
        }
    }
}