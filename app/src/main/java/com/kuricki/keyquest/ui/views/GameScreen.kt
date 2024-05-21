package com.kuricki.keyquest.ui.views

import android.media.midi.MidiManager
import android.os.Build
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.GameScreenViewModel
import com.kuricki.keyquest.ui.components.PianoRoll
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
            RoundedButtonWithIcon(
                onClick = { gameScreenViewModel.midiSelectionOpen(true) },
                icon = Icons.Default.Settings,
                contentDescription = "Midi settings"
            )
            Text(
                text = "Keys: " + gUiState.currPressedKeys,
                style = MaterialTheme.typography.displayMedium,
                modifier = modifier
                    .padding(16.dp)
            )

        }
        PianoRoll(startNote = "C3", endNote = "G4", pressedNotes = gUiState.currPressedKeys, highlightedNotes = mutableSetOf())
    }
    if(gUiState.midiSelectionOpen) {
        MidiDeviceSelection(midiManager = midiManager)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MidiDeviceSelection(
    modifier: Modifier = Modifier,
    midiManager: MidiManager,
    gsvm: GameScreenViewModel = viewModel()
) {
    val gUiState by gsvm.uiState.collectAsState()
    val midiDevices = gsvm.getDevices()

    Dialog(onDismissRequest = {
        gsvm.midiSelectionOpen(false)
    }) {
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
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.selMidiDevice),
                    style = MaterialTheme.typography.labelLarge
                )
                midiDevices.forEach { midiDevice ->
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = gUiState.currMidiDevice == midiDevice,
                            onClick = {
                                gsvm.selectMidiDevice(midiDevice)
                                gsvm.midiSelectionOpen(false)
                            }
                        )
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