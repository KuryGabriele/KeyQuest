package com.kuricki.keyquest.ui.views

import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.data.GameScreenViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    midiManager: MidiManager,
    gameScreenViewModel: GameScreenViewModel = viewModel()
) {
    gameScreenViewModel.start(midiManager)
}