package com.kuricki.keyquest

import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.kuricki.keyquest.ui.views.LoginScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun KeyQuestApp(
    midiManager: MidiManager,
) {
    val context = LocalContext.current
    val navigator = LocalNavigator.current

    Navigator(LoginScreen())
}