package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

data class DisplayNote(
    val pitch: String,
    val positionX: Float,
    val positionY: Float,
    val lined: Boolean
)


@Preview
@Composable
fun MusicSheet (
    modifier: Modifier = Modifier,
    notes: List<DisplayNote> = listOf(
        DisplayNote(
            pitch = "C4",
            positionX = 100f,
            positionY = 300f,
            lined = true
        ),
        DisplayNote(
            pitch = "D4",
            positionX = 200f,
            positionY = 270f,
            lined = false
        )),
) {
    val lineSpacing = 50f
    Surface(
        color = Color.White
    ) {
        Canvas(
            modifier= Modifier.fillMaxSize(),
        ) {
            val width = size.width

            for(i in 1..5) {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, i * lineSpacing),
                    end = Offset(width, i * lineSpacing),
                    strokeWidth = 4f
                )
            }

            for(note in notes) {
                drawCircle(
                    color = Color.Black,
                    radius = 20f,
                    center = Offset(note.positionX, note.positionY)
                )
                if(note.lined) {
                    //draw horizontal line across note
                    drawLine(
                        color = Color.Black,
                        start = Offset(note.positionX-35f, note.positionY),
                        end = Offset(note.positionX+35f, note.positionY),
                        strokeWidth = 10f
                    )
                }
                // draw pitch text
                drawContext.canvas.nativeCanvas.drawText (
                    note.pitch,
                    note.positionX - 20f,
                    375f,
                    Paint().asFrameworkPaint().apply {
                        color = Color.Black.toArgb()
                        textSize = 15.sp.toPx()
                    }
                )
            }
        }
    }
}