package com.kuricki.keyquest.data

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import cafe.adriel.voyager.core.model.ScreenModel
import com.kuricki.keyquest.midiStuff.MyMidiReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GameScreenScreenModel: ScreenModel {
    private lateinit var midiManager: MidiManager
    private val _uiState = MutableStateFlow(GameScreenUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Initialize the view model
     * Will start listening to the midi device if no device is selected
     */
    fun start(mm: MidiManager) {
        midiManager = mm
        //if no device selected, f.e. when the app is opened for the first time
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
                        newDeviceConnected = true
                    )
                }
            }
            //open new device
            midiManager.openDevice(device, listener, null)
        }
    }

    fun getDevices(): MutableSet<MidiDeviceInfo> {
        //fetches all midi devices from the system
        val devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM) //midi 1.0
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

    /**
     * Open the midi selection dialog
     */
    fun midiSelectionOpen(open: Boolean) {
        _uiState.update {
            it.copy(
                midiSelectionOpen = open
            )
        }
    }

    /**
     * No need to show the notification again
     */
    fun deviceConnectedNotificationShown() {
        _uiState.update {
            it.copy(
                newDeviceConnected = false
            )
        }
    }
}