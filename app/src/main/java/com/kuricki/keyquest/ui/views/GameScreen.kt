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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.GameScreenViewModel
import com.kuricki.keyquest.ui.components.MusicSheet
import com.kuricki.keyquest.ui.components.PianoRoll
import com.kuricki.keyquest.ui.components.RoundedButtonWithIcon

data class GameScreen(val midiManager: MidiManager): Screen {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        val modifier = Modifier
        val gameScreenViewModel: GameScreenViewModel = viewModel()
        gameScreenViewModel.start(midiManager)
        val gUiState by gameScreenViewModel.uiState.collectAsState()
        val context = LocalContext.current
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

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
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top bar buttons and info
                //Settings button
                RoundedButtonWithIcon(
                    onClick = { gameScreenViewModel.midiSelectionOpen(true) },
                    icon = Icons.Default.Settings,
                    contentDescription = "Midi settings"
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
                MusicSheet()
            }
            //Piano roll
            PianoRoll(
                modifier = Modifier,
                startNote = "C4",
                endNote = "F5",
                pressedNotes = gUiState.currPressedKeys,
                highlightedNotes = mutableSetOf()
            )
        }
        if(gUiState.midiSelectionOpen) {
            //if midi selection is open, show it
            MidiDeviceSelection()
        }

        if(gUiState.newDeviceConnected) {
            //if a new device is connected, show a toast
            val newDevice = gUiState.currMidiDevice!!.properties.getString("product", "Null")
            Toast.makeText(LocalContext.current, "Connected to: $newDevice", Toast.LENGTH_SHORT).show()
            //reset the flag
            gameScreenViewModel.deviceConnectedNotificationShown()
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
    gsvm: GameScreenViewModel = viewModel()
) {
    val gUiState by gsvm.uiState.collectAsState()
    val midiDevices = gsvm.getDevices() //get the devices from the view model

    // Create a dialog to display the list of MIDI devices
    Dialog(onDismissRequest = {
        gsvm.midiSelectionOpen(false)
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
                                gsvm.selectMidiDevice(midiDevice)
                                gsvm.midiSelectionOpen(false)
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