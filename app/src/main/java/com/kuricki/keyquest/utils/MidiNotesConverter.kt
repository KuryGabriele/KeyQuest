package com.kuricki.keyquest.utils

/**
 * Converts a midi note to a note name
 */
fun midiToNote(midi: Int): String {
    val note = midi % 12
    val octave = midi / 12 - 1
    val noteName = when (note) {
        0 -> "C"
        1 -> "C#"
        2 -> "D"
        3 -> "D#"
        4 -> "E"
        5 -> "F"
        6 -> "F#"
        7 -> "G"
        8 -> "G#"
        9 -> "A"
        10 -> "A#"
        11 -> "B"
        else -> ""
    }

    return "$noteName$octave"
}

/**
 * Converts a note name to a midi note
 */
fun noteToMidi(note: String): Int {
    val octave = note[note.length - 1].toString().toInt() //Extract octave
    val n = note.substring(0, note.length - 1) // Extract note without octave
    val code =  when (n) {
        "C" -> 0
        "C#" -> 1
        "D" -> 2
        "D#" -> 3
        "E" -> 4
        "F" -> 5
        "F#" -> 6
        "G" -> 7
        "G#" -> 8
        "A" -> 9
        "A#" -> 10
        "B" -> 11
        else -> -1
    }

    //return Midi encoded note
    return code + 12 * (octave + 1)
}
