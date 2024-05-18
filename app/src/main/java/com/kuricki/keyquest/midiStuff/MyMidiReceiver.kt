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

        //Multiple key could be pressed at the same time
        var i = 0;
        while(i < receivedData.size) {
            //check if midi signal is key pressed
            if(receivedData[i].toInt() == -112) {
                if(receivedData[i+2].toInt() == 0) {
                    //if key is released, remove it from keysPressedCurrently
                    keysPressedCurrently.remove(MidiToNote(receivedData[i+1].toInt()))
                } else if (receivedData[i+2].toInt() > 0) {
                    //if key is pressed, add it to keysPressedCurrently
                    keysPressedCurrently.add(MidiToNote(receivedData[i+1].toInt()))
                }
            }

            i += 3
        }

        println("Keys Pressed: $keysPressedCurrently")
    }
}