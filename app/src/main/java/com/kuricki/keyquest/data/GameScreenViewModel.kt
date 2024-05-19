package com.kuricki.keyquest.data

import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.kuricki.keyquest.midiStuff.MyMidiReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GameScreenViewModel: ViewModel() {
    private lateinit var midiManager: MidiManager;
    private lateinit var _uiState: MutableStateFlow<GameScreenUiState>
    private lateinit var uiState: StateFlow<GameScreenUiState>

    fun start(mm: MidiManager) {
        midiManager = mm
        var devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)

        val listener = MidiManager.OnDeviceOpenedListener {
            println("Device opened")
            val newPort = it.openOutputPort(0)
            val newMr = MyMidiReceiver()
            newMr.cb = { newSet ->
                updateCurrPressedKey(newSet.toMutableSet())
            }
            newPort.connect(newMr)

            _uiState = MutableStateFlow(GameScreenUiState(
                availableMidiDevices = devices,
                currentMidiDevice = it,
                openedMidiPort = newPort,
                mmr = newMr
            ))
        }

        val device = devices.last()
        println("Device: $device")
        midiManager.openDevice(device, listener, null)
        uiState = _uiState.asStateFlow()
    }

    fun updateCurrPressedKey(newKeys: MutableSet<String>) {
        println("New keys: $newKeys")
        _uiState.update { currentState ->
            currentState.copy(
                currPressedKeys = newKeys
            )
        }
    }
}