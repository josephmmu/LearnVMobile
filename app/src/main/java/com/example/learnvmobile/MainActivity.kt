package com.example.learnvmobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learnvmobile.google.GoogleAuthClient
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.presentation.navigation.AppNavigation
import com.example.learnvmobile.screens.LoginScreen
import com.example.learnvmobile.screens.SetupScreen
import com.example.learnvmobile.ui.theme.LearnVMobileTheme
import com.example.learnvmobile.utils.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels<MainViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //val googleAuthClient = GoogleAuthClient(applicationContext)

        setContent {
            LearnVMobileTheme {

//                var isSignIn by rememberSaveable {
//                    mutableStateOf(googleAuthClient.isSignedIn())
//                }

//                lifecycleScope.launch {
//                    mainViewModel.signIn(this@MainActivity)
//                }

                AppNavigation(
                    mainViewModel = mainViewModel,
                )

            }
        }
    }
}

