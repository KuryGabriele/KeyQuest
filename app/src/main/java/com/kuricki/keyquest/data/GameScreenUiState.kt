package com.kuricki.keyquest.data

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiOutputPort
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.midiStuff.MyMidiReceiver

data class GameScreenUiState(
    val userName: String = "Kury", //user nickname
    val currentLevel: GameLevel = GameLevel(0, "Lvl1: Ode to joy!", 0,0, 0), //current level
    val currMidiDevice: MidiDeviceInfo? = null, //currently opened midi device
    val midiPort: MidiOutputPort? = null, //currently open midi port
    val mmr: MyMidiReceiver = MyMidiReceiver(), //midi receiver and listener
    val currPressedKeys: MutableSet<String> = mutableSetOf(), //pressed keys
    val midiSelectionOpen: Boolean = false, //midi device selection dialog open
    val newDeviceConnected: Boolean = false //true if new midi device connected
)
