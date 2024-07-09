package com.kuricki.keyquest.ui.views

import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.midi.MidiManager
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kuricki.keyquest.KeyquestApplication
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.SummaryScreenModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.UserSession

data class LevelSummaryScreen(val loginSession: UserSession, private val midiManager: MidiManager, val lvl: GameLevel, val score: Int, val error: Int): Screen {
    override val key = uniqueScreenKey

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        //get the repository
        val r = (context.applicationContext as KeyquestApplication).container.gameLevelRepository
        //get the view model
        val summaryScreenModel = rememberScreenModel { SummaryScreenModel(loginSession, context, lvl, score, r) }
        //get the navigator
        val navigator = LocalNavigator.currentOrThrow

        //lock to landscape
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        DisposableEffect(Unit) {
            // Keep the screen on
            (context as? Activity)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            // Hide the status bar
            (context as? Activity)?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            onDispose {
                // Remove the keep screen on flag
                (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                // Show the status bar
                (context as? Activity)?.window?.insetsController?.show(WindowInsets.Type.statusBars())
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        0.0f to MaterialTheme.colorScheme.secondaryContainer,
                        500.0f to MaterialTheme.colorScheme.tertiaryContainer,
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.levelSummary),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = lvl.displayName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(R.string.yourScore) + " " + score.toString() + "/100",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp),
                        text = stringResource(R.string.highScore) + " " + summaryScreenModel.bestScore.intValue + "/100",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp,  bottom = 16.dp),
                        text = stringResource(R.string.errors) + " " + error,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp),
                        onClick = {
                            // navigate to level select screen
                            navigator.replace(GameScreen(loginSession, midiManager, lvl))
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp),
                        onClick = {
                            // navigate to level select screen
                            navigator.replaceAll(LevelSelectScreen(loginSession))
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.continueGame),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
    }

}