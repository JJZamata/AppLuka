package com.puyodev.luka.screens.sign_up

//Clase de estado - Representa y almacena el estado de UI pantalla
data class SignUpUiState(
  val email: String = "prueba123TL@gmail.com",
  val password: String = "Prueba12345",
  val repeatPassword: String = "Prueba12345",
  val username: String = "PruebaEnTestLab"
)
