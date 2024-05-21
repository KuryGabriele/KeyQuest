package com.kuricki.keyquest.data

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.kuricki.keyquest.midiStuff.MyMidiReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GameScreenViewModel: ViewModel() {
    private lateinit var midiManager: MidiManager
    private val _uiState = MutableStateFlow(GameScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun start(mm: MidiManager) {
        midiManager = mm
        //if no device selected
        if(_uiState.value.currMidiDevice == null) {
            //select last device
            val devices = getDevices()
            if(devices.isNotEmpty()) {
                selectMidiDevice(devices.last())
            }
        }
    }

    /**
     * Select a new device and start listening to it
     */
    fun selectMidiDevice(device: MidiDeviceInfo) {
        if(_uiState.value.currMidiDevice != device) {
            //if device is not selected
            println("Device: $device")
            //close old device
            _uiState.value.midiPort?.close()

            //reset pressed keys in case some didn't release
            updateCurrPressedKey(mutableSetOf())
            //create midi listener for new device
            val listener = MidiManager.OnDeviceOpenedListener {
                println("Device opened")
                val newPort = it.openOutputPort(0)
                val newMr = MyMidiReceiver()
                newMr.cb = { newSet ->
                    updateCurrPressedKey(newSet.toMutableSet())
                }
                newPort.connect(newMr)

                _uiState.update { currState ->
                    currState.copy(
                        mmr = newMr,
                        midiPort = newPort,
                        currMidiDevice = device,
                    )
                }
            }
            //open new device
            midiManager.openDevice(device, listener, null)
        }
    }

    fun getDevices(): MutableSet<MidiDeviceInfo> {
        val devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)
        return devices
    }

    /**
     * Update the current pressed keys, called from the midi receiver
     */
    fun updateCurrPressedKey(newKeys: MutableSet<String>) {
        //Update the state with new keys
        _uiState.update {
            //println("Current keys" + it.currPressedKeys + "\nNew keys: " + newKeys)
            it.copy(
                currPressedKeys = newKeys
            )
        }
    }

    fun midiSelectionOpen(open: Boolean) {
        _uiState.update {
            it.copy(
                midiSelectionOpen = open
            )
        }
    }
}