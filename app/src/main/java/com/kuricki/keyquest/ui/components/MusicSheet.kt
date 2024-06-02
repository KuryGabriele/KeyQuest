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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class DisplayNote(
    val pitch: String,
    val positionY: Float,
    val lined: Boolean,
    val alteration: Int = 0 // 0 = no alteration, 1 = sharp, -1 = flat
)

@Preview
@Composable
fun MusicSheet (
    modifier: Modifier = Modifier,
    notes: List<DisplayNote> = listOf(
        DisplayNote(
            pitch = "C4",
            positionY = 300f,
            lined = true,
            alteration = 1
        ),
        DisplayNote(
            pitch = "E5♭",
            positionY = 75f,
            lined = false,
            alteration = -1
        ),
        DisplayNote(
            pitch = "D5",
            positionY = 100f,
            lined = false,
            alteration = 0
        )),
    options: MusicSheetOptions = MusicSheetOptions()
) {
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
                    start = Offset(0f, i * options.pentagramLinesSpacingScaled),
                    end = Offset(width, i * options.pentagramLinesSpacingScaled),
                    strokeWidth = options.pentagramLinesWidthScaled
                )
            }

            // foreach note
            var noteOffset = options.notesOffsetScaled //position of the notes
            for(note in notes) {
                //draw it as a circle
                drawCircle(
                    color = options.notesColor,
                    radius = options.notesRadiusScaled,
                    center = Offset(noteOffset, note.positionY)
                )
                if(note.lined) {
                    //draw horizontal line across note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(noteOffset-options.notesLinedLengthScaled, note.positionY),
                        end = Offset(noteOffset+options.notesLinedLengthScaled, note.positionY),
                        strokeWidth = options.notesLinedWidthScaled
                    )
                }

                if(note.alteration == 1) {
                    // draw sharp text
                    drawContext.canvas.nativeCanvas.drawText (
                        "♯",
                        noteOffset - options.notesRadiusScaled - options.alterationSharpOffsetScaled,
                        note.positionY,
                        Paint().asFrameworkPaint().apply {
                            color = Color.Black.toArgb()
                            textSize = options.alterationSharpFontSizeScaled.toPx()
                        }
                    )
                } else if(note.alteration == -1) {
                    // draw flat text
                    drawContext.canvas.nativeCanvas.drawText (
                        "♭",
                        noteOffset - options.notesRadiusScaled-options.alterationFlatOffsetScaled,
                        note.positionY,
                        Paint().asFrameworkPaint().apply {
                            color = Color.Black.toArgb()
                            textSize = options.alterationFlatFontSizeScaled.toPx()
                        }
                    )
                }

                // check if vertical line can be drawn up
                val verticalLineOffset = noteOffset + options.verticalLineOffsetScaled
                if(note.positionY < options.maxHeightForVerticalLineUpScaled) {
                    // draw vertical line downwards from note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(verticalLineOffset, note.positionY),
                        end = Offset(verticalLineOffset, note.positionY + options.verticalLineLengthScaled),
                        strokeWidth = options.verticalLineWidthScaled
                    )
                } else {
                    //draw vertical line upwards from note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(verticalLineOffset, note.positionY),
                        end = Offset(verticalLineOffset, note.positionY - options.verticalLineLengthScaled),
                        strokeWidth = options.verticalLineWidthScaled
                    )
                }

                // draw pitch text
                drawContext.canvas.nativeCanvas.drawText (
                    note.pitch,
                    noteOffset - options.notesRadiusScaled,
                    options.pitchNotesHeightScaled,
                    Paint().asFrameworkPaint().apply {
                        color = Color.Black.toArgb()
                        textSize = options.pitchNotesSizeScaled.toPx()
                    }
                )

                noteOffset += options.notesOffsetScaled
            }
        }
    }
}

data class MusicSheetOptions(
    val sizeScale: Float = 1f,
    val pentagramLinesColor: Color = Color.Black,
    val pentagramLinesWidth: Float = 4f,
    val pentagramLinesSpacing: Float = 50f,
    val notesColor: Color = Color.Black,
    val notesPitchColor: Color = Color.Black,
    val notesOffset: Float = 100f,
    val notesRadius: Float = 20f,
    val notesHeightOffset: Float = 25f,
    val notesLinedWidth: Float = 10f,
    val notesLinedLength: Float = 30f,
    val maxHeightForVerticalLineUp: Float = 200f,
    val verticalLineLength: Float = 125f,
    val verticalLineOffset: Float = 18f,
    val verticalLineWidth: Float = 5f,
    val pitchNotesHeight: Float = 375f,
    val pitchNotesSize: TextUnit = 15.sp,
    val alterationSharpFontSize: TextUnit = 25.sp,
    val alterationFlatFontSize: TextUnit = 40.sp,
    val alterationSharpOffset: Float = 50f,
    val alterationFlatOffset: Float = 40f,
) {
    val pentagramLinesWidthScaled = pentagramLinesWidth * sizeScale
    val pentagramLinesSpacingScaled = pentagramLinesSpacing * sizeScale
    val notesOffsetScaled = notesOffset * sizeScale
    val notesRadiusScaled = notesRadius * sizeScale
    val notesHeightOffsetScaled = notesHeightOffset * sizeScale
    val notesLinedWidthScaled = notesLinedWidth * sizeScale
    val notesLinedLengthScaled = notesLinedLength * sizeScale
    val maxHeightForVerticalLineUpScaled = maxHeightForVerticalLineUp * sizeScale
    val verticalLineLengthScaled = verticalLineLength * sizeScale
    val verticalLineOffsetScaled = verticalLineOffset * sizeScale
    val verticalLineWidthScaled = verticalLineWidth * sizeScale
    val pitchNotesHeightScaled = pitchNotesHeight * sizeScale
    val pitchNotesSizeScaled = pitchNotesSize * sizeScale
    val alterationSharpFontSizeScaled = alterationSharpFontSize * sizeScale
    val alterationFlatFontSizeScaled = alterationFlatFontSize * sizeScale
    val alterationSharpOffsetScaled = alterationSharpOffset * sizeScale
    val alterationFlatOffsetScaled = alterationFlatOffset * sizeScale
}