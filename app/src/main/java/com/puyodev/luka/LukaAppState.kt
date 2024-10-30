package com.puyodev.luka

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.common.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

//stable por decir que es estable y no va a sufrir cambios en frecuencia
@Stable
class LukaAppState(
    val snackbarState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarState.showSnackbar(text)
                snackbarManager.clearSnackbarState()
            }
        }
    }

    //util para regresar a la pantalla anterior en la pila de navegación.
    fun popUp() {
        navController.popBackStack()
    }

    //Navega a la pantalla especificada en route, pero evita cargar varias instancias de la misma pantalla usando launchSingleTop = true.
    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    //Navega a la pantalla en route y elimina (popUpTo) todas las pantallas en la pila de navegación hasta la pantalla indicada en popUp. La opción inclusive = true elimina también la pantalla destino (popUp).
    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    //Limpia toda la pila de navegación (popUpTo(0)) y navega a la pantalla indicada en route. Esto es útil para iniciar una nueva pila de navegación, por ejemplo, al reiniciar la aplicación o cerrar sesión.
    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

//Gestiona la visualización de mensajes de Snackbar en tiempo real y ofrece métodos prácticos para navegar y manipular la pila de navegación: volver, navegar a una sola instancia, limpiar pila