package com.kuricki.keyquest.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.kuricki.keyquest.ui.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
){
    val loginUiState by loginViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column (
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            TextField(
                value = loginUiState.usrName,
                onValueChange = { loginViewModel.setUsrName(it) },
                label = { Text(stringResource(R.string.username)) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = loginUiState.psw,
                onValueChange = { loginViewModel.setPsw(it) },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = modifier
                    .width(100.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ }) {
                Text(
                    stringResource(R.string.login),
                    style = MaterialTheme.typography.bodyLarge
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typography.labelLarge,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = modifier
                    .width(120.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ }) {
                Text(
                    stringResource(R.string.register),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}