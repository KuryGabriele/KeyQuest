package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kuricki.keyquest.midiStuff.MidiToNote
import com.kuricki.keyquest.midiStuff.NoteToMidi
import kotlin.math.roundToInt

// Inspired by github.com/fluxtah/pianoroll
@Composable
fun PianoRoll(
    modifier: Modifier = Modifier,
    startNote: String,
    endNote: String,
    pressedNotes: MutableSet<String>,
    highlightedNotes: MutableSet<String>,
    options: PianoRollOptions = PianoRollOptions()
){
    val midiStartIndex = NoteToMidi(startNote)
    val midiEndIndex = NoteToMidi(endNote)
    val notes: MutableList<String> = mutableListOf()

    var naturalNotesCount = 0
    var alteredNotesCount = 0

    //Populate notes list
    for (i in midiStartIndex..midiEndIndex){
        val note = MidiToNote(i)
        if(note.contains("#")) {
            alteredNotesCount++
        } else {
            naturalNotesCount++
        }
        notes.add(note)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        var xPos = options.keyMarginScaled/2
        notes.forEachIndexed { index, note ->
            val isAltered = note.contains("#")
            val isHighlighted = highlightedNotes.contains(note)
            val isPressed = pressedNotes.contains(note)

            if(!isAltered && index > 0) {
                xPos += (options.keyWidthScaled + options.keyMarginScaled).roundToInt()
            }

            Box(
                modifier = Modifier
                    .width(if (isAltered) options.alteredKeyWidthScaled.dp else options.keyWidthScaled.dp)
                    .height(if (isAltered) options.alteredKeyHeightScaled.dp else options.keyHeightScaled.dp)
                    .zIndex(if (isAltered) 2f else 1f)
                    .offset(
                        x = (xPos + if (isAltered) (options.alteredKeyWidthScaled).roundToInt() else 0).dp,
                        y = options.topBorderSizeScaled.dp
                    )
                    .border(1.dp, options.borderColor)
                    .background(computeKeyColor(options, isHighlighted, isPressed, isAltered)),
                contentAlignment = Alignment.BottomCenter,
            ) {
                if(options.showNoteNames) {
                    Text(
                        text = note.replace("#", "♯"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if(isAltered) options.alteredKeyTextColor else options.keyTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun computeKeyColor(options: PianoRollOptions, isHighlighted: Boolean, isPressed: Boolean, isAltered: Boolean): Color {
    return if(isHighlighted && isPressed) {
        options.correctKeyColor
    } else if(isHighlighted) {
        MaterialTheme.colorScheme.primary
        //options.highlightedKeyColor
    } else if(isPressed) {
        MaterialTheme.colorScheme.tertiary
        //options.pressedKeyColor
    } else {
        if(isAltered) options.alteredKeyColor else options.keyColor
    }
}

data class PianoRollOptions (
    val sizeScale: Float = 1.0f,
    val keyWidth: Float = 64f,
    val alteredKeyWidth: Float = 44f,
    val keyMargin: Float = 4f,
    val keyHeight: Float = 128f,
    val alteredKeyHeight: Float = 80f,
    val topBorderSize: Float = 4f,
    val bottomBorderSize: Float = 4f,
    val fontSize: Float = 12f,
    val showNoteNames: Boolean = true,
    val alteredKeyColor: Color = Color.Black,
    val keyColor: Color = Color.White,
    val highlightedKeyColor: Color = Color.Cyan,
    val pressedKeyColor: Color = Color.Magenta,
    val correctKeyColor: Color = Color.Green,
    val borderColor: Color = Color.Black,
    val keyTextColor: Color = Color.Black,
    val alteredKeyTextColor: Color = Color.White
) {
    val keyWidthScaled = keyWidth * sizeScale
    val alteredKeyWidthScaled = alteredKeyWidth * sizeScale
    val keyMarginScaled = keyMargin * sizeScale
    val keyHeightScaled = keyHeight * sizeScale
    val alteredKeyHeightScaled = alteredKeyHeight * sizeScale
    val topBorderSizeScaled = topBorderSize * sizeScale
    val bottomBorderSizeScaled = bottomBorderSize * sizeScale
    val fontSizeScaled = fontSize * sizeScale
}