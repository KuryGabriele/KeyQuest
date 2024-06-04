package com.kuricki.keyquest.ui.views

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.AppViewModelProvider
import com.kuricki.keyquest.data.LevelSelectViewModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.UserSession

@Composable
fun LevelSelectScreen(
    modifier: Modifier = Modifier,
    onLevelSelected: (Int) -> Unit = {},
    levelSelectViewModel: LevelSelectViewModel = viewModel(factory = AppViewModelProvider.Factory),
    loginSession: UserSession,
) {
    val lsUiState by levelSelectViewModel.uiState.collectAsState()
    levelSelectViewModel.setUserName(loginSession.username)

    val activity = (LocalContext.current as? Activity)
    BackHandler {
        println("Back pressed")
        activity?.finish()
    }
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
                LevelCard(modifier, onLevelSelected, levelSelectViewModel, lvl)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LevelCard(
    modifier: Modifier,
    onLevelSelected: (Int) -> Unit = {},
    levelSelectViewModel: LevelSelectViewModel = viewModel(),
    lvl: GameLevel,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .width(350.dp)
            .clickable { onLevelSelected(lvl.id) }
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = lvl.displayName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier
                    .padding(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.highScore) + lvl.bestScore + "/100",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                when (lvl.difficulty) {
                    0 -> {
                        Text(
                            text = stringResource(R.string.easy),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Green,
                        )
                    }
                    1 -> {
                        Text(
                            text = stringResource(R.string.medium),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Yellow,
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(R.string.hard),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Red,
                        )
                    }
                }
            }
        }
    }
}
