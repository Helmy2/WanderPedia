package com.example.wanderpedia.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.theme.WanderPediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        val viewModel: MainViewModel by viewModels()

        splashScreen.setKeepOnScreenCondition {
            viewModel.showOnboarding.value == null
        }

        setContent {
            val isShowOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()

            WanderPediaTheme {
                if (isShowOnboarding != null) {
                    WonderNav(showOnboarding = isShowOnboarding!!)
                }
            }
        }
    }
}

