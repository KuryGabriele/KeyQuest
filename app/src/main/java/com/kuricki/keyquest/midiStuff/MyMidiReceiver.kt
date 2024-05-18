package com.kuricki.keyquest.midiStuff

import android.media.midi.MidiReceiver

class MyMidiReceiver:MidiReceiver() {
    var keysPressedCurrently = mutableSetOf<String>()
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        // Process the received MIDI data
        // For example, print the MIDI data to the log
        val receivedData = data.copyOfRange(offset, offset + count)
        //check if receivedData[1] is not null
        //println("MIDI Data Received: ${receivedData.joinToString(", ")}")
        if(receivedData[0].toInt() == -112 && receivedData.size > 1 && receivedData[2] >= 0) {
            val note = MidiToNote(receivedData[1].toInt())
            if(receivedData[2] > 0) {
                keysPressedCurrently.add(note)
            } else {
                keysPressedCurrently.remove(note)
            }
            println("Keys Pressed: $keysPressedCurrently")
        }
    }
}