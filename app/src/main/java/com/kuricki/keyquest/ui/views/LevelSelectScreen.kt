package com.kuricki.keyquest.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.GameLevel
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
            style = MaterialTheme.typography.displayLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
        ) {
            lsUiState.levels.forEach { lvl ->
                LevelCard(modifier, levelSelectViewModel, lvl)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LevelCard(
    modifier: Modifier,
    levelSelectViewModel: LevelSelectViewModel = viewModel(),
    lvl: GameLevel
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .size(350.dp, 150.dp)
            .clickable { }
    ) {
        Column (
            modifier = modifier
        ) {
            Text(
                text = lvl.displayName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier
                    .padding(16.dp)
            )
            Text(
                text = stringResource(R.string.highScore) + lvl.bestScore + "/100",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .padding(16.dp)
            )
        }
    }
}