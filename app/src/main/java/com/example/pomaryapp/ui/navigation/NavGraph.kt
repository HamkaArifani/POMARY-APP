package com.example.pomaryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.pomaryapp.data.local.preferences.AuthPreferences
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.ui.auth.LoginScreen
import com.example.pomaryapp.ui.home.HomeScreen
import com.example.pomaryapp.ui.pin.PinScreen

@Composable
fun NavGraph(
    authRepository: AuthRepository,
    navController: NavHostController,
    initialRoute: String
) {
    val isLoggedIn = authRepository.isUserLoggedIn()
    val isSetupCompleted by authRepository.isSetupCompleted().collectAsState(initial = false)
    NavHost(
        navController = navController,
        startDestination = initialRoute
    ){
        composable("login"){
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("pin"){
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("pin"){
            PinScreen(
                isSetupMode = !isSetupCompleted,
                onSuccess = {
                    navController.navigate("home"){
                        popUpTo("pin") {inclusive = true}
                    }
                }
            )
        }

        composable("home"){
            HomeScreen(navController = navController)
        }
    }
}