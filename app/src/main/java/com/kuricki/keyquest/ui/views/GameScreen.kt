package com.kuricki.keyquest.ui.views

import android.media.midi.MidiManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.GameScreenViewModel
import com.kuricki.keyquest.ui.components.MusicSheet
import com.kuricki.keyquest.ui.components.RoundedButtonWithIcon

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    midiManager: MidiManager,
    gameScreenViewModel: GameScreenViewModel = viewModel()
) {
    gameScreenViewModel.start(midiManager)
    val gUiState by gameScreenViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Top bar buttons and info
            //Settings button
            RoundedButtonWithIcon(
                onClick = { gameScreenViewModel.midiSelectionOpen(true) },
                icon = Icons.Default.Settings,
                contentDescription = "Midi settings"
            )
            //Pressed keys text
            Text(
                text = "Keys: " + gUiState.currPressedKeys,
                style = MaterialTheme.typography.displayMedium,
                modifier = modifier
                    .padding(16.dp)
            )
        }
        //Music sheet card
        Card (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            MusicSheet()
        }
        //Piano roll
        //PianoRoll(startNote = "C3", endNote = "G4", pressedNotes = gUiState.currPressedKeys, highlightedNotes = mutableSetOf())
    }
    if(gUiState.midiSelectionOpen) {
        //if midi selection is open, show it
        MidiDeviceSelection()
    }

    if(gUiState.newDeviceConnected) {
        Toast.makeText(LocalContext.current, "Connected to: " + gUiState.currMidiDevice!!.properties.getString("product", "asd"), Toast.LENGTH_SHORT).show()
        gameScreenViewModel.deviceConnectedNotificationShown()
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
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Display a title for the dialog
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.selMidiDevice),
                    style = MaterialTheme.typography.labelLarge
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
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}