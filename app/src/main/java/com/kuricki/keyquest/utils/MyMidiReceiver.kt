package com.kuricki.keyquest.utils

import android.media.midi.MidiReceiver

class MyMidiReceiver:MidiReceiver() {
    var keysPressedCurrently = mutableSetOf<String>()
    var cb: (Set<String>) -> Unit = {}
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        // Get the midi data, its a byte array, I convert it to Int for easier handling
        val receivedData = data.copyOfRange(offset, offset + count)

        //Multiple key could be pressed at the same time
        var i = 0;
        var changed = false;
        while(i < receivedData.size) {
            //check if midi signal is key pressed
            if(receivedData[i].toInt() == -112) {
                if(receivedData[i+2].toInt() == 0) {
                    //if key is released, remove it from keysPressedCurrently
                    val a = keysPressedCurrently.remove(MidiToNote(receivedData[i+1].toInt()))
                    if(a) {
                        // if key was removed, set changed to true
                        changed = true
                    }
                } else if (receivedData[i+2].toInt() > 0) {
                    //if key is pressed, add it to keysPressedCurrently
                    val a = keysPressedCurrently.add(MidiToNote(receivedData[i+1].toInt()))
                    if(a) {
                        // if key was added, set changed to true
                        changed = true
                    }
                }
            }

            i += 3
        }

        if(changed) {
            //call cb only if the set changed
            cb(keysPressedCurrently)
        }
    }
}