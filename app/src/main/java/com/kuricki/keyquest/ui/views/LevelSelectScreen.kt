package com.kuricki.keyquest.ui.views

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.midi.MidiManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kuricki.keyquest.KeyquestApplication
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.LevelSelectScreenModel
import com.kuricki.keyquest.data.LoginScreenModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.UserSession

data class LevelSelectScreen(val loginSession: UserSession): Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val modifier = Modifier
        val context = LocalContext.current
        //get the repository
        val r = (context.applicationContext as KeyquestApplication).container.gameLevelRepository
        //get the view model
        val levelSelectViewModel = rememberScreenModel { LevelSelectScreenModel(r) }
        //get the ui state
        val lsUiState by levelSelectViewModel.uiState.collectAsState()
        //set the username
        levelSelectViewModel.setUserName(loginSession.username)
        val navigator = LocalNavigator.currentOrThrow

        //Unlock the orientation
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        if(lsUiState.getLevels){
            levelSelectViewModel.getLevels()
        }
        val onLevelSelected: (GameLevel) -> Unit = { lvl ->
            println("Level selected: ${lvl.id}")
            val midiManager: MidiManager = context.getSystemService(Context.MIDI_SERVICE) as MidiManager
            navigator.push(GameScreen(midiManager = midiManager, lvl))
        }



        Scaffold(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        0.0f to MaterialTheme.colorScheme.secondaryContainer,
                        500.0f to MaterialTheme.colorScheme.tertiaryContainer,
                    )
                ),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                        stringResource(R.string.greeting) + " " + lsUiState.userName,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = modifier
                        .fillMaxWidth(),
                    actions = {
                        val loginRepo = (context.applicationContext as KeyquestApplication).container.userSessionRepository
                        val loginScreenModel = rememberScreenModel(tag = "login") { LoginScreenModel(loginRepo) }
                        IconButton(
                            onClick = {
                                //Logout
                                loginScreenModel.logout {
                                    //Delete levels from db
                                    levelSelectViewModel.deleteLevels()
                                    //Replace the login screen
                                    navigator.replaceAll(LoginScreen())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                )
            }
        ) { ip ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(ip)
                    .background(
                        Brush.linearGradient(
                            0.0f to MaterialTheme.colorScheme.secondaryContainer,
                            500.0f to MaterialTheme.colorScheme.tertiaryContainer,
                        )
                    ),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.Center,
                ) {
                    lsUiState.levels.forEach { lvl ->
                        LevelCard(modifier, onLevelSelected, lvl)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

    }
}

@Composable
fun LevelCard(
    modifier: Modifier,
    onLevelSelected: (GameLevel) -> Unit = {},
    lvl: GameLevel,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .width(350.dp)
            .clickable { onLevelSelected(lvl) }
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
                    1 -> {
                        Text(
                            text = stringResource(R.string.easy),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Green,
                        )
                    }
                    2 -> {
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
