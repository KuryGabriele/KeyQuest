package com.kuricki.keyquest.midiStuff

import android.media.midi.MidiReceiver

class MyMidiReceiver:MidiReceiver() {
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        // Process the received MIDI data
        // For example, print the MIDI data to the log
        val receivedData = data.copyOfRange(offset, offset + count)
        //check if receivedData[1] is not null
        if(receivedData.size > 1 && receivedData[2] > 0) {
            val note = MidiToNote(receivedData[1].toInt())
            println("MIDI Data Received: $note")
        }
    }
}