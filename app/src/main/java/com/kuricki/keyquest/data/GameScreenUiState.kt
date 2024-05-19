package com.kuricki.keyquest.data

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiOutputPort
import com.kuricki.keyquest.midiStuff.MyMidiReceiver

data class GameScreenUiState(
    val userName: String = "Kury",
    val currentLevel: GameLevel = GameLevel(0, "Lvl1: Ode to joy!", 0,0, 0),
    val currentMidiDevice: MidiDevice,
    val availableMidiDevices: MutableSet<MidiDeviceInfo>,
    val openedMidiPort: MidiOutputPort,
    val mmr: MyMidiReceiver,
    val currPressedKeys: MutableSet<String> = mutableSetOf<String>()
)
