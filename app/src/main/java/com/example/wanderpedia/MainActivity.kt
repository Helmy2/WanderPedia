package com.example.wanderpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wanderpedia.core.ui.theme.WanderPediaTheme
import com.example.wanderpedia.navigation.WonderNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        setContent {
            var isReady by rememberSaveable {
                mutableStateOf(false)
            }
            splashScreen.setKeepOnScreenCondition { !isReady }
            WanderPediaTheme {
                WonderNav { isReady = true }
            }
        }
    }
}

