package com.example.learnvmobile.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.screens.HomeScreen
import com.example.learnvmobile.screens.LoginScreen
import com.example.learnvmobile.screens.SetupScreen
import com.example.learnvmobile.utils.Routes

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.name
    ) {

        // FROM Login Screen TO Home Screen
        composable(route = Screen.LoginScreen.name) {
            LoginScreen(
                mainViewModel = mainViewModel,
                onLogin = {userId ->
                    navController.navigate("${Screen.HomeScreen.name}/$userId")
                },
                toRegister = {userId ->
                    navController.navigate("${Screen.SetupScreen.name}/$userId")
                }
                )}

        // FROM SetupScreen TO Home Screen
        composable(route = "${Screen.SetupScreen.name}/{userId}") {
            SetupScreen(
                mainViewModel = mainViewModel,
                onRegister = { userId ->
                    Log.d("Navigation", "Navigating to HomeScreen with userId: $userId")
                    navController.navigate("${Screen.HomeScreen.name}/$userId")
                },
                onBack = {navController.popBackStack()}
            )}


        composable(
            route = "${Screen.HomeScreen.name}/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) {
                navBackStackEntry ->
            navBackStackEntry.arguments?.getInt("userId").let{ id->
                HomeScreen (userId = id!!,
                    mainViewModel = mainViewModel,
                    navController = navController,
                    onBack = {navController.popBackStack()})
            }
        }







    }

}