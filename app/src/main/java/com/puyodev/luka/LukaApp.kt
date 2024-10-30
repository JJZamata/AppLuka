package com.puyodev.luka

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.puyodev.luka.common.snackbar.SnackbarManager
//import com.puyodev.luka.screens.AppContent
import com.puyodev.luka.screens.pay.PayScreen
import com.puyodev.luka.screens.profile.ProfileScreen
//import com.example.makeitso.screens.edit_task.EditTaskScreen
import com.puyodev.luka.screens.login.LoginScreen
//import com.puyodev.luka.screens.settings.SettingsScreen
import com.puyodev.luka.screens.sign_up.SignUpScreen
import com.puyodev.luka.screens.splash.SplashScreen
//import com.example.makeitso.screens.tasks.TasksScreen
import com.puyodev.luka.ui.theme.LukaTheme
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
//import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope

@Composable
fun LukaApp() {
    LukaTheme {
        /*Para solicitar permiso de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }
        */
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            //estructura de la pantalla
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                },
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    lukaGraph(appState)
                }
            }
        }
    }
}

/* funcion para obtener el permiso muestar un dialog al user
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}
*/

//estado de la aplicacion
@Composable
fun rememberAppState(
    snackbarState: SnackbarHostState = remember { SnackbarHostState() }, // Cambia a SnackbarHostState
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    //recordaremos estos parametros y almacenado en LukaAppState clase que facilita asi su acceso a lo largo de la aplicación
    remember(snackbarState, navController, snackbarManager, resources, coroutineScope) {
        LukaAppState(snackbarState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
//Permite acceso a los recursos actuales del contexto
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

//se define el gráfico de navegación - se agrupa todas las rutas y screens respectivas
fun NavGraphBuilder.lukaGraph(appState: LukaAppState) {


    /*
    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }
*/
    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGNUP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(PAY_SCREEN){
        PayScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(PROFILE_SCREEN){
        ProfileScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    /*
    composable(TASKS_SCREEN) { TasksScreen(openScreen = { route -> appState.navigate(route) }) }

    composable(
        route = "$EDIT_TASK_SCREEN$TASK_ID_ARG",
        arguments = listOf(navArgument(TASK_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        EditTaskScreen(
            popUpScreen = { appState.popUp() }
        )
    }
    */
}
