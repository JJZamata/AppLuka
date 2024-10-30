package com.puyodev.luka.screens.sign_up

//Clase de estado - Representa y almacena el estado de UI pantalla
data class SignUpUiState(
  val email: String = "",
  val password: String = "",
  val repeatPassword: String = "",
  val username: String = ""
)
