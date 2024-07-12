package com.kuricki.keyquest.utils

import android.media.midi.MidiInputPort
import okio.IOException

class MyMidiSender() {
    var outPort: MidiInputPort? = null

    constructor(outputPort: MidiInputPort) : this() {
        outPort = outputPort
    }

    fun send(note: String, duration: Long) {
        val noteOn = byteArrayOf(0x90.toByte(), noteToMidi(note).toByte(), 64.toByte())
        val noteOff = byteArrayOf(0x80.toByte(), noteToMidi(note).toByte(), 0.toByte())

        try {
            outPort?.send(noteOn, 0, noteOn.size)
            Thread.sleep(duration)
            outPort?.send(noteOff, 0, noteOff.size)
        } catch (e: IOException) {
            //port could be closed
            println("MidiSender: $note, port closed")
        } catch (e: Exception) {
            println("MidiSender: $note, $e")
        }
    }

    fun stopAll() {
        //may not work on some midi devices
        val allNotesOff = byteArrayOf(0x90.toByte(), 0x7B.toByte(), 0.toByte())

        try {
            outPort?.send(allNotesOff, 0, allNotesOff.size)
        } catch (e: IOException) {
            //port could be closed
            println("MidiSender port closed")
        } catch (e: Exception) {
            println("MidiSender: $e")
        }
    }
}