package com.kuricki.keyquest.ui.views

import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.midi.MidiManager
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.GameScreenScreenModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.UserSession
import com.kuricki.keyquest.ui.components.MusicSheet
import com.kuricki.keyquest.ui.components.PianoRoll
import com.kuricki.keyquest.ui.components.RoundedButtonWithIcon

data class GameScreen(val loginSession: UserSession, val midiManager: MidiManager, val lvl: GameLevel): Screen {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        val modifier = Modifier
        //get the view model
        val gameScreenScreenModel = rememberScreenModel { GameScreenScreenModel(midiManager, lvl) }
        //get the ui state
        val gUiState by gameScreenScreenModel.uiState.collectAsState()
        val context = LocalContext.current
        //lock the screen orientation to landscape
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        if(gUiState.levelFinished) {
            //if level finished, go to level summary screen
            val navigator = LocalNavigator.currentOrThrow
            navigator.replaceAll(LevelSummaryScreen(loginSession, gUiState.currentLevel, gUiState.currentScore, gUiState.errors))
        }

        DisposableEffect(Unit) {
            // Keep the screen on
            (context as? Activity)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            // Hide the status bar
            (context as? Activity)?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            onDispose {
                // Remove the keep screen on flag
                (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                // Show the status bar
                (context as? Activity)?.window?.insetsController?.show(WindowInsets.Type.statusBars())
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        0.0f to MaterialTheme.colorScheme.secondaryContainer,
                        500.0f to MaterialTheme.colorScheme.tertiaryContainer,
                    )
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Top bar buttons and info
                //Settings button
                RoundedButtonWithIcon(
                    onClick = { gameScreenScreenModel.midiSelectionOpen(true) },
                    icon = Icons.Default.Settings,
                    contentDescription = "Midi settings",
                )

                Text(
                    text = gUiState.currentLevel.displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = gUiState.currentScore.toString() + "/100",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            //Music sheet card
            Card (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                //show the music sheet
                MusicSheet(notes = gUiState.keysToPress.take(20).toMutableList())
            }

            PianoRoll(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                startNote = gUiState.lowestNote,
                endNote = gUiState.highestNote,
                pressedNotes = gUiState.currPressedKeys,
                // highlight notes in the piano roll
                highlightedNotes = if(gUiState.keysToPress.isEmpty())  mutableSetOf() else mutableSetOf(gUiState.keysToPress[0])
            )
        }
        if(gUiState.midiSelectionOpen) {
            //if midi selection is open, show it
            MidiDeviceSelection(gssm = gameScreenScreenModel)
        }

        if(gUiState.newDeviceConnected) {
            //if a new device is connected, show a toast
            val newDevice = gUiState.currMidiDevice!!.properties.getString("product", "Null")
            Toast.makeText(LocalContext.current, "Connected to: $newDevice", Toast.LENGTH_SHORT).show()
            //reset the flag
            gameScreenScreenModel.deviceConnectedNotificationShown()
        }
    }
}

/**
 * Displays a list of MIDI devices and allows the user to select one.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MidiDeviceSelection(
    modifier: Modifier = Modifier,
    gssm: GameScreenScreenModel
) {
    val gUiState by gssm.uiState.collectAsState()
    val midiDevices = gssm.getDevices() //get the devices from the view model

    // Create a dialog to display the list of MIDI devices
    Dialog(onDismissRequest = {
        gssm.midiSelectionOpen(false)
    }) {
        // Create a card to display the list of MIDI devices
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Display a title for the dialog
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.selMidiDevice),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                // Display a list of MIDI devices
                midiDevices.forEach { midiDevice ->
                    //as a row with a radio button and text
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Display a radio button
                        RadioButton(
                            selected = gUiState.currMidiDevice == midiDevice,
                            onClick = {
                                //when the radio button is clicked, select the device
                                //and close the dialog
                                gssm.selectMidiDevice(midiDevice)
                                gssm.midiSelectionOpen(false)
                            }
                        )
                        // Display the name of the MIDI device
                        Text(
                            text = midiDevice.properties.getString("product", "asd"),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if(midiDevices.isEmpty()) {
                    //if no devices are found, show a toast
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(R.string.noMidiDevices),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}