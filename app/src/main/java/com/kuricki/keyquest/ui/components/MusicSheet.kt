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
    val pitch: String
)

fun pitchToHeight(pitch: String, options: MusicSheetOptions): Float {
    //remove alterations
    val pitch = pitch.replace("♯", "").replace("♭", "")

    val note = pitch[0] //get che note
    val octave = pitch[1].toString().toInt() //get the octave

    //calculate the height
    val height = when(octave) {
        3 -> 12
        4 -> 6
        5 -> -1
        else -> 6
    }
    //calculate the offset
    val offset = when(note) {
        'A' -> 1
        'G' -> 2
        'F' -> 3
        'E' -> 4
        'D' -> 5
        'C' -> 6
        'B' -> 0
        else -> 1
    }

    return (height + offset)*options.notesHeightOffsetScaled
}

fun isNoteLined(pitch: String, options: MusicSheetOptions): Int {
    val pitch = pitch.replace("♯", "").replace("♭", "")
    if(pitch == "C4" || pitch == "A5") {
        //Draw a line across
        return 1
    }

    if(pitch == "A3") {
        //Draw two lines one across and one above
        return 3
    }

    if(pitch == "B3") {
        //Draw a line above
        return 2
    }

    //no lines
    return 0;
}

@Preview
@Composable
fun MusicSheet (
    modifier: Modifier = Modifier,
    notes: List<DisplayNote> = listOf(
        DisplayNote(
            pitch = "B3"
        ),
        DisplayNote(
            pitch = "E♭5"
        ),
        DisplayNote(
            pitch = "D5"
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
                //get the height of the note
                val positionY = pitchToHeight(note.pitch, options)
                //get the alteration
                var alteration = 0;
                if(note.pitch.contains("♯")) {
                    alteration = 1
                } else if(note.pitch.contains("♭")) {
                    alteration = -1
                }

                //line status for note
                val lined = isNoteLined(note.pitch, options)

                //draw it as a circle
                drawCircle(
                    color = options.notesColor,
                    radius = options.notesRadiusScaled,
                    center = Offset(noteOffset, positionY)
                )
                if(lined == 1) {
                    //draw horizontal line across note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(noteOffset-options.notesLinedLengthScaled, positionY),
                        end = Offset(noteOffset+options.notesLinedLengthScaled, positionY),
                        strokeWidth = options.notesLinedWidthScaled
                    )
                } else if(lined == 2) {
                    //draw line above note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(noteOffset-options.notesLinedLengthScaled, positionY-options.notesHeightOffsetScaled),
                        end = Offset(noteOffset+options.notesLinedLengthScaled, positionY-options.notesHeightOffsetScaled),
                        strokeWidth = options.notesLinedWidthScaled/2
                    )
                } else if(lined == 3) {
                    //draw line across note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(noteOffset-options.notesLinedLengthScaled, positionY),
                        end = Offset(noteOffset+options.notesLinedLengthScaled, positionY),
                        strokeWidth = options.notesLinedWidthScaled
                    )
                    //draw helping line
                    drawLine(
                        color = options.notesColor,
                        start = Offset(noteOffset-options.notesLinedLengthScaled, positionY-options.pentagramLinesSpacingScaled+15),
                        end = Offset(noteOffset+options.notesLinedLengthScaled, positionY-options.pentagramLinesSpacingScaled+15),
                        strokeWidth = options.notesLinedWidthScaled/2
                    )
                }

                if(alteration == 1) {
                    // draw sharp text
                    drawContext.canvas.nativeCanvas.drawText (
                        "♯",
                        noteOffset - options.notesRadiusScaled - options.alterationSharpOffsetScaled,
                        positionY,
                        Paint().asFrameworkPaint().apply {
                            color = Color.Black.toArgb()
                            textSize = options.alterationSharpFontSizeScaled.toPx()
                        }
                    )
                } else if(alteration == -1) {
                    // draw flat text
                    drawContext.canvas.nativeCanvas.drawText (
                        "♭",
                        noteOffset - options.notesRadiusScaled-options.alterationFlatOffsetScaled,
                        positionY,
                        Paint().asFrameworkPaint().apply {
                            color = Color.Black.toArgb()
                            textSize = options.alterationFlatFontSizeScaled.toPx()
                        }
                    )
                }

                // check if vertical line can be drawn up
                val verticalLineOffset = noteOffset + options.verticalLineOffsetScaled
                if(positionY < options.maxHeightForVerticalLineUpScaled) {
                    // draw vertical line downwards from note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(verticalLineOffset, positionY),
                        end = Offset(verticalLineOffset, positionY + options.verticalLineLengthScaled),
                        strokeWidth = options.verticalLineWidthScaled
                    )
                } else {
                    //draw vertical line upwards from note
                    drawLine(
                        color = options.notesColor,
                        start = Offset(verticalLineOffset, positionY),
                        end = Offset(verticalLineOffset, positionY - options.verticalLineLengthScaled),
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