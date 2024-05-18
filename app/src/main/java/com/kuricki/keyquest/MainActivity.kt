package com.kuricki.keyquest

import android.content.Context
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.media.midi.MidiOutputPort
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.kuricki.keyquest.midiStuff.MyMidiReceiver
import com.kuricki.keyquest.ui.theme.KeyQuestTheme

class MainActivity : ComponentActivity() {
    private lateinit var midiManager: MidiManager
    private lateinit var midiDevices: MutableSet<MidiDeviceInfo>
    private lateinit var openedDevice: MidiDevice
    private lateinit var openedPort: MidiOutputPort
    private lateinit var mr: MyMidiReceiver

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        midiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
        // get all midi devices
        midiDevices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)

        // open the first device
        val device = midiDevices.last()
        println("Device name: $device")
        val listener = MidiManager.OnDeviceOpenedListener {
            println("Device opened")
            openedDevice = it
            openedPort = openedDevice.openOutputPort(0)
            mr = MyMidiReceiver()
            openedPort.connect(mr)
        }

        midiManager.openDevice(device, listener, null)

        setContent {
            KeyQuestTheme {
                KeyQuestApp()
            }
        }
    }
}