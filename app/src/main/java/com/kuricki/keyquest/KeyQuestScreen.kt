package com.kuricki.keyquest

import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.midi.MidiManager
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.kuricki.keyquest.data.LoginSession
import com.kuricki.keyquest.ui.views.GameScreen
import com.kuricki.keyquest.ui.views.LevelSelectScreen
import com.kuricki.keyquest.ui.views.LoginScreen

/**
 * Enum class for all the screens in the app
 */
enum class KeyQuestScreens() {
    Login(), //login screen
    LevelSelect(), //level selection screen
    Game(), // game screen
    Summary() // summary screen with score
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun KeyQuestApp(
    midiManager: MidiManager,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    var loginSession: LoginSession? = null

    //scaffolding for the router
    Scaffold(){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = KeyQuestScreens.Login.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ){
            //login screen
            composable(route = KeyQuestScreens.Login.name) {
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                LoginScreen(
                    onLoginSuccess = {
                        println("Login success $it.id")
                        loginSession = it
                        navController.navigate(KeyQuestScreens.LevelSelect.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            //level selection screen
            composable(route = KeyQuestScreens.LevelSelect.name) {
                if(loginSession == null) {
                    navController.navigate(KeyQuestScreens.Login.name)
                }
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


                loginSession?.let { it1 ->
                    LevelSelectScreen(
                        modifier = Modifier.fillMaxHeight(),
                        onLevelSelected = { level: Int ->
                            //navigate to the game screen with the selected level
                            println("Level selected: $level")
                            navController.navigate(KeyQuestScreens.Game.name)
                        },
                        loginSession = it1
                    )
                }
            }
            //game screen
            composable(route = KeyQuestScreens.Game.name) {
                (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                GameScreen(
                    midiManager = midiManager,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}