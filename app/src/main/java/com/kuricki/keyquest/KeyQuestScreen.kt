package com.kuricki.keyquest

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
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
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(KeyQuestScreens.LevelSelect.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = KeyQuestScreens.LevelSelect.name) {
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                LevelSelectScreen(
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = KeyQuestScreens.Game.name) {
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                LevelSelectScreen(
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}