package com.kuricki.keyquest.data

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import cafe.adriel.voyager.core.model.ScreenModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.utils.MyMidiReceiver
import com.kuricki.keyquest.utils.midiToNote
import com.kuricki.keyquest.utils.noteToMidi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.ceil
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GameScreenModel(private val midiManager: MidiManager, private val lvl: GameLevel): ScreenModel {
    private val _uiState = MutableStateFlow(GameScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        //if no device selected, f.e. when the app is opened for the first time
        if(_uiState.value.currMidiDevice == null) {
            //select last device
            val devices = getDevices()
            if(devices.isNotEmpty()) {
                selectMidiDevice(devices.last())
            }
        }

        //Piano roll
        var lowestNote = Int.MAX_VALUE
        var highestNote = Int.MIN_VALUE
        lvl.notes.split(" ").forEach {
            val n = noteToMidi(it)
            if(n < lowestNote) {
                lowestNote = n
            }
            if(n > highestNote) {
                highestNote = n
            }
        }

        _uiState.update {
            it.copy(
                currentLevel = lvl,
                keysToPress = lvl.notes.split(" ").toMutableList(),
                lowestNote = midiToNote(lowestNote),
                highestNote = midiToNote(highestNote)
            )
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

    /**
     * Get all midi devices
     */
    fun getDevices(): MutableSet<MidiDeviceInfo> {
        val devices = midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM) //midi 1.0
        devices.addAll(midiManager.getDevicesForTransport(MidiManager.TRANSPORT_UNIVERSAL_MIDI_PACKETS)) //midi 2.0
        return devices
    }

    /**
     * Update the current pressed keys, called from the midi receiver
     */
    private fun updateCurrPressedKey(newKeys: MutableSet<String>) {
        //Update the state with new keys
        _uiState.update {
            //println("Current keys" + it.currPressedKeys + "\nNew keys: " + newKeys)
            it.copy(
                currPressedKeys = newKeys
            )
        }

        if(uiState.value.keysToPress.isNotEmpty() && newKeys.contains(uiState.value.keysToPress[0])) {
            //correct key pressed
            _uiState.update {
                it.copy(
                    waitForKeyOff = true
                )
            }
        } else {
            if(_uiState.value.waitForKeyOff) {
                //key off
                _uiState.update {
                    it.copy(
                        notesDone = it.notesDone + 1
                    )
                }
                //get next notes
                var newNotes = uiState.value.currentLevel.notes.split(" ").toMutableList()
                var score = uiState.value.currentScore
                if(!uiState.value.wrongNotePressed) {
                    //update score
                    score += ceil(100/newNotes.size.toDouble()).toInt()
                }

                score = min(score, 100)
                _uiState.update {
                    it.copy(
                        currentScore = score,
                        wrongNotePressed = false
                    )
                }

                //get notes done
                val notesDone = uiState.value.notesDone
                //remove done notes
                newNotes = newNotes.drop(notesDone).toMutableList()


                if(newNotes.isEmpty()){
                    //level finished
                    if(score >= uiState.value.currentLevel.bestScore) {
                        //update best score
                        _uiState.update {
                            it.copy(
                                currentLevel = it.currentLevel.copy(
                                    bestScore = score
                                ),
                            )
                        }
                    }
                    //make level finish
                    _uiState.update {
                        it.copy(
                            levelFinished = true,
                            keysToPress = mutableListOf(),
                        )
                    }
                } else {
                    //reset waitForKeyOff and update ui
                    _uiState.update {
                        it.copy(
                            waitForKeyOff = false,
                            keysToPress = newNotes,
                        )
                    }
                }
            } else {
                if(newKeys.isNotEmpty()) {
                    //wrong key pressed
                    _uiState.update {
                        it.copy(
                            errors = it.errors + 1,
                            wrongNotePressed = true
                        )
                    }
                }
            }
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