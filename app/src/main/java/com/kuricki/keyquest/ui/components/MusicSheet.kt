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
            positionY = 300f,
            lined = true
        ),
        DisplayNote(
            pitch = "D3",
            positionY = 100f,
            lined = false
        ),
        DisplayNote(
            pitch = "D3",
            positionY = 100f,
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

            // draw the pentagram
            for(i in 1..5) {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, i * lineSpacing),
                    end = Offset(width, i * lineSpacing),
                    strokeWidth = 4f
                )
            }

            // foreach note
            var noteOffset = 100f //position of the notes
            for(note in notes) {
                //draw it as a circle
                drawCircle(
                    color = Color.Black,
                    radius = 20f,
                    center = Offset(noteOffset, note.positionY)
                )
                if(note.lined) {
                    //draw horizontal line across note
                    drawLine(
                        color = Color.Black,
                        start = Offset(noteOffset-35f, note.positionY),
                        end = Offset(noteOffset+35f, note.positionY),
                        strokeWidth = 10f
                    )
                }

                // check if vertical line can be drawn up
                if(note.positionY < 200) {
                    // draw vertical line downwards from note
                    drawLine(
                        color = Color.Black,
                        start = Offset(noteOffset+18f, note.positionY),
                        end = Offset(noteOffset+18f, note.positionY+125f),
                        strokeWidth = 5f
                    )
                } else {
                    //draw vertical line upwards from note
                    drawLine(
                        color = Color.Black,
                        start = Offset(noteOffset+18f, note.positionY),
                        end = Offset(noteOffset+18f, note.positionY-125f),
                        strokeWidth = 5f
                    )
                }

                // draw pitch text
                drawContext.canvas.nativeCanvas.drawText (
                    note.pitch,
                    noteOffset - 20f,
                    375f,
                    Paint().asFrameworkPaint().apply {
                        color = Color.Black.toArgb()
                        textSize = 15.sp.toPx()
                    }
                )

                noteOffset += 100f
            }
        }
    }
}