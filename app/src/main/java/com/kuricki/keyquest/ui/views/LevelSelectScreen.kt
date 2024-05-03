package com.kuricki.keyquest.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.data.LevelSelectViewModel

@Composable
fun LevelSelectScreen(
    modifier: Modifier = Modifier,
    levelSelectViewModel: LevelSelectViewModel = viewModel()
) {
    val levelUiState by levelSelectViewModel.uiState.collectAsState()
}