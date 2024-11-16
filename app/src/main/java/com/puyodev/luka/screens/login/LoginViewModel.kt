package com.puyodev.luka.screens.login

import androidx.compose.runtime.mutableStateOf
import com.puyodev.luka.LOGIN_SCREEN
import com.puyodev.luka.SIGNUP_SCREEN
import com.puyodev.luka.R.string as AppText
import com.puyodev.luka.PAY_SCREEN
import com.puyodev.luka.common.ext.isValidEmail
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.model.service.AccountService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val accountService: AccountService,
  logService: LogService
) : LukaViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (password.isBlank()) {
      SnackbarManager.showMessage(AppText.empty_password_error)
      return
    }

    launchCatching {
      accountService.authenticate(email, password)
      // Si es exitoso, navega a SettingsScreen y elimina LoginScreen de la pila
      openAndPopUp(PAY_SCREEN, LOGIN_SCREEN)
    }
  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }

  fun onCreateAccountClick(openAndPopUp: (String, String) -> Unit) {
      // Si es exitoso, navega a SignUpScreen y elimina LoginScreen de la pila
    openAndPopUp(SIGNUP_SCREEN, LOGIN_SCREEN)
  }
}
