package com.puyodev.luka.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.puyodev.luka.R.string as AppText
import com.puyodev.luka.common.composable.*
import com.puyodev.luka.common.ext.basicButton
import com.puyodev.luka.common.ext.fieldModifier
import com.puyodev.luka.common.ext.textButton
import com.puyodev.luka.ui.theme.LukaTheme

@Composable
fun LoginScreen(
  openAndPopUp: (String, String) -> Unit,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  LoginScreenContent(
    uiState = uiState,
    onEmailChange = viewModel::onEmailChange,
    onPasswordChange = viewModel::onPasswordChange,
    onSignInClick = { viewModel.onSignInClick(openAndPopUp) },
    onForgotPasswordClick = viewModel::onForgotPasswordClick,
    onCreateAccountClick = { viewModel.onCreateAccountClick(openAndPopUp) },
  )
}

@Composable
fun LoginScreenContent(
  modifier: Modifier = Modifier,
  uiState: LoginUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSignInClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  onCreateAccountClick: () -> Unit,
) {
  BasicToolbar(AppText.login_details)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(
      uiState.email,
      onEmailChange,
      Modifier.fieldModifier("email_field")
    )

    PasswordField(
      uiState.password,
      onPasswordChange,
      Modifier.fieldModifier("password_field")
    )
    BasicButton(AppText.sign_in, Modifier.basicButton()) { onSignInClick() }

    BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
      onForgotPasswordClick()
    }
    BasicTextButton(AppText.start_to_create_account, Modifier.textButton()) {
      onCreateAccountClick()
    }
  }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  val uiState = LoginUiState(
    email = "correo123@gmail.com"
  )

  LukaTheme {
    LoginScreenContent(
      uiState = uiState,
      onEmailChange = { },
      onPasswordChange = { },
      onSignInClick = { },
      onForgotPasswordClick = { },
      onCreateAccountClick = { }
    )
  }
}
