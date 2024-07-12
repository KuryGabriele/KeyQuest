package com.kuricki.keyquest.utils

import android.media.midi.MidiReceiver

/**
 * Class for receiving midi signals, calls a callback function when a key is pressed or released
 */
class MyMidiReceiver:MidiReceiver() {
    private var keysPressedCurrently = mutableSetOf<String>()
    var cb: (Set<String>) -> Unit = {} //callback function

    /**
     * Called when a midi signal is received
     */
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        // Get the midi data, its a byte array, I convert it to Int for easier handling
        val receivedData = data.copyOfRange(offset, offset + count)

        //Multiple key could be pressed at the same time
        var i = 0
        var changed = false
        while(i < receivedData.size) {
            //check if midi signal is key pressed
            if(receivedData[i].toInt() == -112) {
                if(receivedData[i+2].toInt() == 0) {
                    //if key is released, remove it from keysPressedCurrently
                    val a = keysPressedCurrently.remove(midiToNote(receivedData[i+1].toInt()))
                    if(a) {
                        // if key was removed, set changed to true
                        changed = true
                    }
                } else if (receivedData[i+2].toInt() > 0) {
                    //if key is pressed, add it to keysPressedCurrently
                    val a = keysPressedCurrently.add(midiToNote(receivedData[i+1].toInt()))
                    if(a) {
                        // if key was added, set changed to true
                        changed = true
                    }
                }
            }

            //increase by three because each event has 3 bytes
            i += 3
        }

        if(changed) {
            //call cb only if the set changed
            cb(keysPressedCurrently)
        }
    }
}