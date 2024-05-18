package com.kuricki.keyquest.midiStuff

import android.media.midi.MidiReceiver

class MyMidiReceiver:MidiReceiver() {
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        // Process the received MIDI data
        // For example, print the MIDI data to the log
        val receivedData = data.copyOfRange(offset, offset + count)
        println("MIDI Data Received: ${receivedData.joinToString(", ")}")
    }
}