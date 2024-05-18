package com.kuricki.keyquest.midiManager

import android.content.Context
import android.content.pm.PackageManager

class MidiManager {
    fun isDeviceCompatible(context: Context): Boolean {
        // Check if the user device has MIDI support
        return (context.packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI));
    }

    
}