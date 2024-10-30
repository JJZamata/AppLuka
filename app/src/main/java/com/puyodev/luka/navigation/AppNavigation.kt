package com.puyodev.luka.navigation

import androidx.compose.runtime.Composable//nomina
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost//controller
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.puyodev.luka.screens.sign_up.SignUpScreen
//import com.puyodev.luka.screens.splash.AnimationLoadScreen
import com.puyodev.luka.screens.PaymentScreen
//import com.puyodev.luka.screens.AppContent//nombre de las funciones
import com.puyodev.luka.screens.HistorialView
import com.puyodev.luka.screens.ProfileScreen
import com.puyodev.luka.screens.login.LoginScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(route=AppScreens.SplashScreen.route){
            //AnimationLoadScreen(navController)
        }
        composable(route=AppScreens.MainScreen.route){
            //AppContent(navController)
        }
        //agregando signup and login
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(openAndPopUp = { route, popUp ->
                navController.navigate(route) {
                    launchSingleTop = true
                    popUpTo(popUp) { inclusive = true }
                }
            })
        }

        composable(route = AppScreens.SignUpScreen.route) {
            SignUpScreen(openAndPopUp = { route, popUp ->
                navController.navigate(route) {
                    launchSingleTop = true
                    popUpTo(popUp) { inclusive = true }
                }
            })
        }

        composable(route=AppScreens.ProfileScreen.route){
            ProfileScreen(navController)
        }
        composable(route=AppScreens.HistoryScreen.route){
            HistorialView(navController)
        }
        composable(route=AppScreens.PaymentScreen.route +"/{num_bus}/{address}/{lukitas}/{fecha}",
            arguments = listOf(
                navArgument(name = "num_bus"){type = NavType.IntType },
                navArgument(name = "address"){type = NavType.StringType },
                navArgument(name = "lukitas"){type = NavType.IntType },
                navArgument(name = "fecha"){type = NavType.StringType },

            )){
                backStackEntry ->
            // Extraer los argumentos del backStackEntry
            val numBus = backStackEntry.arguments?.getInt("num_bus") ?: 0
            val address = backStackEntry.arguments?.getString("address") ?: ""
            val lukitas = backStackEntry.arguments?.getInt("lukitas") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""

            // Pasar los valores a PaymentScreen
            PaymentScreen(
                navController = navController,
                num_bus = numBus,
                address = address,
                lukitas = lukitas,
                fecha = fecha
            )
        }
    }
}