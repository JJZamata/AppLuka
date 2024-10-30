/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.puyodev.luka.screens.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf//De Compose, permite crear un estado observable para la UI.
import com.puyodev.luka.LOGIN_SCREEN
import com.puyodev.luka.PAY_SCREEN
import com.puyodev.luka.SIGNUP_SCREEN
import com.puyodev.luka.R.string as AppText
import com.puyodev.luka.common.ext.isValidEmail
import com.puyodev.luka.common.ext.isValidPassword
import com.puyodev.luka.common.ext.isValidUsername
import com.puyodev.luka.common.ext.passwordMatches
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.model.service.AccountService//autenticacion de cuenta
import com.puyodev.luka.model.service.LogService//para logs
import com.puyodev.luka.navigation.AppScreens
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel//inyeccion de dependencias
import javax.inject.Inject

@HiltViewModel//marcando clase para Hilt
class SignUpViewModel @Inject constructor(
  private val accountService: AccountService,//inyeccion de servicios
  logService: LogService
) : LukaViewModel(logService) {
  var uiState = mutableStateOf(SignUpUiState())//observando los campos del form
    private set//solo cambios de uistate por dentro del viewmodel

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password
  private val username
    get() = uiState.value.username

  //actualizando los valores de email,password,etc. en uistate
  fun onUsernameChange(newValue: String) {
    uiState.value = uiState.value.copy(username = newValue)
  }

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onRepeatPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(repeatPassword = newValue)
  }

  fun onLoginAccountClick(openAndPopUp: (String, String) -> Unit) {
    // Para navegar a LoginScreen y elimina SignUpScreen de la pila
    openAndPopUp(LOGIN_SCREEN,SIGNUP_SCREEN)
  }

  //al presionar el boton de envio - proceso de validacion y creacion de cuenta y redireccion
  fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
    if (!username.isValidUsername()) {
      SnackbarManager.showMessage(AppText.username_error)
      return
    }

    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (!password.isValidPassword()) {
      SnackbarManager.showMessage(AppText.password_error)
      return
    }

    if (!password.passwordMatches(uiState.value.repeatPassword)) {
      SnackbarManager.showMessage(AppText.password_match_error)
      return
    }
    Log.d("SignUpViewModel", "All validations passed, attempting to link account")

    //creacion de la cuenta - corrutina
    launchCatching {
      try {
        accountService.createAccount(email, password, username)
        Log.d("SignUpViewModel", "Creación de la cuenta lograda")
        openAndPopUp(PAY_SCREEN, SIGNUP_SCREEN)
      } catch (e: Exception) {
        Log.e("SignUpViewModel", "Creacion de la cuenta fallida", e)
        SnackbarManager.showMessage(AppText.sign_in)//cambiar o crear valor sign_error
      }
    }
  }
}