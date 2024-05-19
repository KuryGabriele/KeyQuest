package com.kuricki.keyquest.data

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
        val devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)

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
                )
            }
        }

        if(_uiState.value.currMidiDevice == null) {
            //if device is already selected
            val device = devices.last()
            println("Device: $device")
            _uiState.update { currState ->
                currState.copy(
                    currMidiDevice = device,
                )
            }
            midiManager.openDevice(device, listener, null)
        }
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