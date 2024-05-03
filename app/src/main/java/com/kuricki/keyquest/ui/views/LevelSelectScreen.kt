package com.kuricki.keyquest.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.LevelSelectViewModel

@Composable
fun LevelSelectScreen(
    modifier: Modifier = Modifier,
    levelSelectViewModel: LevelSelectViewModel = viewModel()
) {
    val lsUiState by levelSelectViewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.greeting) + " " + lsUiState.userName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = modifier,
        ) {

        }
    }
}