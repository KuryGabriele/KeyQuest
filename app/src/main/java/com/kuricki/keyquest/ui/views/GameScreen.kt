package com.kuricki.keyquest.ui.views

import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.data.GameScreenViewModel
import com.kuricki.keyquest.ui.components.PianoRoll

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
        Text(
            text = "Keys: " + gUiState.currPressedKeys,
            style = MaterialTheme.typography.displayMedium,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
        PianoRoll(startNote = "C3", endNote = "C4", pressedNotes = gUiState.currPressedKeys, highlightedNotes = mutableSetOf())
    }
}