package com.example.learnvmobile.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learnvmobile.google.GoogleAuthClient
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.screens.HomeScreen
import com.example.learnvmobile.screens.LessonListScreen
import com.example.learnvmobile.screens.LoginScreen
import com.example.learnvmobile.screens.SetupScreen
import com.example.learnvmobile.utils.Routes

@RequiresApi(Build.VERSION_CODES.O)
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
                onLogin = {userId -> navController.navigate("${Screen.HomeScreen.name}/$userId") },
                toRegister = {userId -> navController.navigate("${Screen.SetupScreen.name}/$userId") },
                onSignInSuccess = {navController.navigate(Screen.HomeScreen)}
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
            arguments = listOf(
                navArgument("userId") {
                type = NavType.IntType
            })
        ) {
                navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getInt("userId") ?: return@composable

                HomeScreen (userId = userId,
                    mainViewModel = mainViewModel,
                    navController = navController,
                    onBack = {navController.popBackStack()},
                    onCourseSelected = { courseId ->
                        navController.navigate("${Screen.LessonScreen.name}/$userId/$courseId")
                    })

        }

        composable(
            route = "${Screen.LessonScreen.name}/{userId}/{courseId}",
            arguments = listOf(
                navArgument("userId") {type = NavType.IntType},
                navArgument("courseId") {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable

            LessonListScreen(
                mainViewModel = mainViewModel,
                userId = userId,
                courseId = courseId,
                onBack = {
                    Log.d("LoginScreen", "Navigating to HomeScreen with userId: $userId")

                    navController.navigate("${Screen.HomeScreen.name}/$userId") {
                        popUpTo(Screen.HomeScreen.name) { inclusive = false }
                    }
                })
        }
    } // end of navHost

}