package com.kuricki.keyquest

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kuricki.keyquest.ui.views.LevelSelectScreen
import com.kuricki.keyquest.ui.views.LoginScreen


enum class KeyQuestScreens() {
    Login(),
    LevelSelect(),
    Game(),
    Summary()
}

@Composable
fun KeyQuestApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = KeyQuestScreens.Login.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ){
            composable(route = KeyQuestScreens.Login.name) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(KeyQuestScreens.LevelSelect.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = KeyQuestScreens.LevelSelect.name) {
                LevelSelectScreen(
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}