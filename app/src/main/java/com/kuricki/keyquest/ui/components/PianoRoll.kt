package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Inspired by github.com/fluxtah/pianoroll
@Composable
fun PianoRoll(
    modifier: Modifier = Modifier,
    startNote: String,
    endNote: String,
    pressedNotes: MutableSet<String>,
    highlightedNotes: MutableSet<String>,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    )
}