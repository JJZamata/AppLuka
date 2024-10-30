package com.puyodev.luka.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuthException
import com.puyodev.luka.LOGIN_SCREEN
import com.puyodev.luka.SIGNUP_SCREEN
import com.puyodev.luka.SPLASH_SCREEN
import com.puyodev.luka.model.service.AccountService
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log // Asegúrate de importar Log
import com.puyodev.luka.PAY_SCREEN

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : LukaViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        Log.d("SplashViewModel", "Inicializando SplashViewModel")
        launchCatching {
            Log.d("SplashViewModel", "Fetching configuration")
            configurationService.fetchConfiguration()
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        showError.value = false
        val userExists = accountService.hasUser
        Log.d("SplashViewModel", "onAppStart llamado, accountService.hasUser = $userExists")

        if (userExists) {
            Log.d("SplashViewModel", "Redirigiendo a SIGNUP_SCREEN")
            openAndPopUp(PAY_SCREEN, SPLASH_SCREEN)
        } else {
            Log.d("SplashViewModel", "Redirigiendo a LOGIN_SCREEN")
            openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
        }
    }
}