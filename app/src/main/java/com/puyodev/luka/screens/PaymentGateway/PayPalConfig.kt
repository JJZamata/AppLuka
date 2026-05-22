package com.puyodev.luka.screens.PaymentGateway

import android.app.Application
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * PayPalConfig - VERSIÓN DEMO (STUB)
 *
 * Esta clase es un stub que permite compilar el proyecto sin la dependencia de PayPal.
 * Para habilitar PayPal real:
 * 1. Descomentar la dependencia en build.gradle.kts
 * 2. Restaurar los imports y funciones originales
 * 3. Quitar los comentarios del código
 */
@Singleton
class PayPalConfig @Inject constructor(
    private val application: Application
) {

    companion object {
        // Credenciales de PayPal (guardadas para futuro uso)
        const val CLIENT_ID = "ATsSK7UfAy4TVOhGf_5lEHkKwaUrT8k8fze7uZcdliN0qwJZGYbYDXAZCrIPANcx7nQCx6MpwWEg4e0a"
        const val RETURN_URL = "com.puyodev.luka://paypalpay"
        const val SECRET_KEY = "EEcuT_r7MLajWJzNYS72q-mSI3Yn_JzSfIXsuFEkuFXddjT8BQvbgVSrnNTuvIQcHmvjEZsuJJlIErAX"
    }

    fun initialize() {
        Log.d("PayPalConfig", "PayPal deshabilitado - Modo DEMO")
    }

    fun cleanup() {
        Log.d("PayPalConfig", "PayPal cleanup - Modo DEMO (no-op)")
    }
}
