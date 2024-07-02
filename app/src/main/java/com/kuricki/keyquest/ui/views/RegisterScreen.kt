package com.kuricki.keyquest.ui.views

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.AppViewModelProvider
import com.kuricki.keyquest.data.LoginViewModel

class RegisterScreen: Screen {
    @Composable
    override fun Content() {
        val modifier = Modifier
        val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
        val loginUiState by loginViewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        //Check if there is a session saved
        if(loginUiState.checkSession) {
            loginViewModel.checkSession { session ->
                //Replace the login screen
                session?.let {
                    println(session)
                    navigator.replaceAll(LevelSelectScreen(loginSession = session))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Bottom,
            ) {
                //Username
                TextField(
                    value = loginUiState.usrName,
                    onValueChange = { loginViewModel.setUsrName(it) },
                    label = { Text(stringResource(R.string.username)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrect = false, imeAction = ImeAction.Next),
                    singleLine = true,
                    isError = loginUiState.error != null,
                )
                Spacer(modifier = Modifier.height(16.dp))
                //Password
                TextField(
                    value = loginUiState.psw,
                    onValueChange = { loginViewModel.setPsw(it) },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    singleLine = true,
                    isError = loginUiState.error != null,
                )
                Spacer(modifier = Modifier.height(16.dp))
                //Repeat password
                TextField(
                    value = loginUiState.psw2,
                    onValueChange = { loginViewModel.setPsw2(it) },
                    label = { Text(stringResource(R.string.repeatPassword)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        loginViewModel.registerUser { session ->
                            //Replace the register screen
                            navigator.replaceAll(LevelSelectScreen(loginSession = session))
                        }
                    }),
                    singleLine = true,
                    isError = loginUiState.error != null,
                    supportingText = {
                        if (loginUiState.error != null) {
                            Text(loginUiState.error.toString())
                        }
                    },
                )
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                ){
                    //Register button
                    Button(
                        modifier = modifier
                            .width(150.dp)
                            .height(50.dp),
                        onClick = {
                            loginViewModel.registerUser { session ->
                                //Go to login
                                navigator.replaceAll(LevelSelectScreen(loginSession = session))
                            }
                        }) {
                        Text(
                            stringResource(R.string.register),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
                //Already has an account
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text(
                        stringResource(R.string.haveAccount),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    ClickableText(
                        text = AnnotatedString(stringResource(R.string.login)),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        ),
                        modifier = modifier
                            .padding(horizontal = 8.dp),
                        onClick = {
                            //clear errors
                            loginViewModel.setError(null)
                            //navigate back to login
                            navigator.pop()
                        }
                    )
                }
            }
        }
    }
}