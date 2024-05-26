package com.kuricki.keyquest.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuricki.keyquest.R
import com.kuricki.keyquest.data.LoginSession
import com.kuricki.keyquest.data.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (session: LoginSession) -> Unit = {},
    loginViewModel: LoginViewModel = viewModel()
){
    val loginUiState by loginViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(32.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column (
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = loginUiState.usrName,
                onValueChange = { loginViewModel.setUsrName(it) },
                label = { Text(stringResource(R.string.username)) },
                singleLine = true,
                isError = loginUiState.error != null
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = loginUiState.psw,
                onValueChange = { loginViewModel.setPsw(it) },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = loginUiState.error != null,
                supportingText = {
                    if (loginUiState.error != null) {
                        Text(loginUiState.error.toString())
                    }
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            ){
                Button(
                    modifier = modifier
                        .width(150.dp)
                        .height(50.dp),
                    onClick = { loginViewModel.loginUser(onLoginSuccess) }) {
                    Text(
                        stringResource(R.string.login),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}