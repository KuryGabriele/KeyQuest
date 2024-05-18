package com.kuricki.keyquest.midiManager
import android.content.Context
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.media.midi.MidiManager.TRANSPORT_MIDI_BYTE_STREAM
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MidiActivity: AppCompatActivity() {
    private lateinit var midiManager: MidiManager
    private lateinit var midiDevices: MutableSet<MidiDeviceInfo>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        midiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
        // get all midi devices
        midiDevices = midiManager.getDevicesForTransport(TRANSPORT_MIDI_BYTE_STREAM)

        //open midi device

    }
}