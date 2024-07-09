package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.kuricki.keyquest.utils.MidiToNote
import com.kuricki.keyquest.utils.NoteToMidi
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
    val midiStartIndex = NoteToMidi(startNote) // starting note
    val midiEndIndex = NoteToMidi(endNote) // ending note
    val notes: MutableList<String> = mutableListOf() // list of notes

    var naturalNotesCount = 0
    var alteredNotesCount = 0

    //Populate notes list
    for (i in midiStartIndex..midiEndIndex){
        val note = MidiToNote(i)
        if(note.contains("#")) {
            // if altered note
            alteredNotesCount++
        } else {
            // if natural note
            naturalNotesCount++
        }
        // add note to list
        notes.add(note)
    }

    //Piano roll layout
    Box(
        modifier = modifier
            //Fix width so tha I can center it
            .width(naturalNotesCount * (options.keyWidthScaled + options.keyMarginScaled).roundToInt().dp)
            .background(Color.Transparent)
    ) {
        // starting position of keys
        var xPos = options.keyMarginScaled/2
        // foreach notes
        notes.forEachIndexed { index, note ->
            val isAltered = note.contains("#")
            val isHighlighted = highlightedNotes.contains(note)
            val isPressed = pressedNotes.contains(note)

            if(!isAltered && index > 0) {
                // if altered shift the key
                xPos += (options.keyWidthScaled + options.keyMarginScaled).roundToInt()
            }

            // draw piano key
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
                    // show note name on top of the key
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
        // if user is pressing the correct key
        options.correctKeyColor
    } else if(isHighlighted) {
        // the key the user is supposed to press
        MaterialTheme.colorScheme.primary
        //options.highlightedKeyColor
    } else if(isPressed) {
        // the key the user pressed, but not the correct one
        MaterialTheme.colorScheme.tertiary
        //options.pressedKeyColor
    } else {
        // normal key
        if(isAltered) options.alteredKeyColor else options.keyColor
    }
}

data class PianoRollOptions (
    val sizeScale: Float = 1.0f, //general size scaling factor
    val keyWidth: Float = 64f, //starting width for natural key
    val alteredKeyWidth: Float = 44f, //starting width for altered key
    val keyMargin: Float = 4f, //starting margin between keys
    val keyHeight: Float = 128f, //starting height for natural key
    val alteredKeyHeight: Float = 80f, //starting height for altered key
    val topBorderSize: Float = 4f, //starting top border size
    val bottomBorderSize: Float = 4f, //starting bottom border size
    val fontSize: Float = 12f, //starting font size
    val showNoteNames: Boolean = true, //show note names on top of keys
    val alteredKeyColor: Color = Color.Black, //color for altered keys
    val keyColor: Color = Color.White, //color for natural keys
    val highlightedKeyColor: Color = Color.Cyan, //color for highlighted keys
    val pressedKeyColor: Color = Color.Magenta, //color for pressed keys
    val correctKeyColor: Color = Color.Green, //color for correct keys
    val borderColor: Color = Color.Black, //color for border around alteredKey
    val keyTextColor: Color = Color.Black, //color for key text
    val alteredKeyTextColor: Color = Color.White //color for altered key text
) {
    //scale all values
    val keyWidthScaled = keyWidth * sizeScale
    val alteredKeyWidthScaled = alteredKeyWidth * sizeScale
    val keyMarginScaled = keyMargin * sizeScale
    val keyHeightScaled = keyHeight * sizeScale
    val alteredKeyHeightScaled = alteredKeyHeight * sizeScale
    val topBorderSizeScaled = topBorderSize * sizeScale
    val bottomBorderSizeScaled = bottomBorderSize * sizeScale
    val fontSizeScaled = fontSize * sizeScale
}