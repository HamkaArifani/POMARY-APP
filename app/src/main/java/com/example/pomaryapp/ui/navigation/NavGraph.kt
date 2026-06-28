package com.example.pomaryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.ui.auth.LoginScreen
import com.example.pomaryapp.ui.home.HomeScreen
import com.example.pomaryapp.ui.order.form.OrderFormScreen
import com.example.pomaryapp.ui.pin.PinScreen
import com.example.pomaryapp.ui.preorder.detail.PreorderDetailScreen
import com.example.pomaryapp.ui.preorder.form.PreorderFormScreen
import com.example.pomaryapp.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    authRepository: AuthRepository,
    navController: NavHostController,
    initialRoute: String
) {
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

        composable("settings") {
            SettingsScreen(navController = navController)
        }

        composable(
            route = "preorder_detail/{preorderId}",
            arguments = listOf(navArgument("preorderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("preorderId") ?: ""
            PreorderDetailScreen(preorderId = id, navController = navController )
        }

        composable(
            route = "preorder_form?preorderId={preorderId}",
            arguments = listOf(navArgument("preorderId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("preorderId")
            PreorderFormScreen(preorderId = id, navController = navController)
        }


        composable(
            route = "order_form?preorderId={preorderId}&orderId={orderId}",
            arguments = listOf(
                navArgument("preorderId") { type = NavType.StringType },
                navArgument("orderId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val poId = backStackEntry.arguments?.getString("preorderId") ?: ""
            val oId = backStackEntry.arguments?.getString("orderId")
            OrderFormScreen(preorderId = poId, orderId = oId, navController = navController)
        }
    }
}