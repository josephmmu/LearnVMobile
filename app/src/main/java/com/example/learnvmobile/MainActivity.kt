package com.example.learnvmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.presentation.navigation.AppNavigation
import com.example.learnvmobile.screens.LoginScreen
import com.example.learnvmobile.screens.SetupScreen
import com.example.learnvmobile.ui.theme.LearnVMobileTheme
import com.example.learnvmobile.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnVMobileTheme {

                AppNavigation(mainViewModel = mainViewModel)

            }
        }
    }
}

