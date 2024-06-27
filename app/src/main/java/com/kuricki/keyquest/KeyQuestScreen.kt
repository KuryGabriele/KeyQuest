package com.kuricki.keyquest

import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kuricki.keyquest.ui.views.LoginScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun KeyQuestApp(
    midiManager: MidiManager,
) {
    Navigator(LoginScreen()) { n ->
        SlideTransition(n)
    }
}