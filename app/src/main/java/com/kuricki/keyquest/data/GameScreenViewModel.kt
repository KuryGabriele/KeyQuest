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
    private lateinit var midiManager: MidiManager;
    private val _uiState = MutableStateFlow(GameScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun start(mm: MidiManager) {
        midiManager = mm
    }

    fun selectMidiDevice(device: MidiDeviceInfo) {
        println("Selecting new device: $device")
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
                )
            }
        }

        if(_uiState.value.currMidiDevice != device) {
            //if device is already selected
            println("Device: $device")
            //close old device
            _uiState.value.midiPort?.close()

            _uiState.update { currState ->
                currState.copy(
                    currMidiDevice = device,
                )
            }
            midiManager.openDevice(device, listener, null)
        }
    }

    fun getDevices(): MutableSet<MidiDeviceInfo> {
        val devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)
        return devices
    }

    fun updateCurrPressedKey(newKeys: MutableSet<String>) {
        _uiState.update {
            println("Current keys" + it.currPressedKeys + "New keys: " + newKeys)
            it.copy(
                currPressedKeys = newKeys
            )
        }
    }
}