package com.kuricki.keyquest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kuricki.keyquest.ui.views.SplashScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun KeyQuestApp() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    0.0f to MaterialTheme.colorScheme.secondaryContainer,
                    500.0f to MaterialTheme.colorScheme.tertiaryContainer,
                )
            ),
    ){
        Navigator(SplashScreen()) { n ->
            SlideTransition(n)
        }
    }

}