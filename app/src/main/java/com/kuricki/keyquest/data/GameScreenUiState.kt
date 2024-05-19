package com.kuricki.keyquest.data

import android.media.midi.MidiDeviceInfo
import com.kuricki.keyquest.midiStuff.MyMidiReceiver

data class GameScreenUiState(
    val userName: String = "Kury",
    val currentLevel: GameLevel = GameLevel(0, "Lvl1: Ode to joy!", 0,0, 0),
    val currMidiDevice: MidiDeviceInfo? = null,
    val mmr: MyMidiReceiver = MyMidiReceiver(),
    val currPressedKeys: MutableSet<String> = mutableSetOf<String>()
)
