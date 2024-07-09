package com.kuricki.keyquest.ui.views

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kuricki.keyquest.KeyquestApplication
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.LoginScreenModel

class SplashScreen: Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val modifier = Modifier
        val r = (context.applicationContext as KeyquestApplication).container.userSessionRepository
        val loginScreenModel = rememberScreenModel(tag = "login") { LoginScreenModel(r) }
        val loginUiState by loginScreenModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        //Check if there is a session saved
        if(loginUiState.checkSession) {
            loginScreenModel.checkSession(context) { session ->
                session?.let {
                    //If session is not null, replace the login screen
                    navigator.replaceAll(LevelSelectScreen(loginSession = session))
                }

                if(session == null) {
                    //If session is null, replace the login screen
                    navigator.replaceAll(LoginScreen())
                }
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
                ),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = true,
                enter = fadeIn(animationSpec = tween(2000)),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(32.dp)
                        .animateContentSize(),
                )
            }
        }
    }
}