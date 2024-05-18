package com.kuricki.keyquest.midiStuff

fun MidiToNote(midi: Int): String {
    val note = midi % 12
    val octave = midi / 12 - 1
    var noteName = when (note) {
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

fun NoteToMidi(note: String): Int {
    val octave = note[note.length - 1].toString().toInt()
    val note = note.substring(0, note.length - 1)
    var code =  when (note) {
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

    return code + 12 * (octave + 1)
}
