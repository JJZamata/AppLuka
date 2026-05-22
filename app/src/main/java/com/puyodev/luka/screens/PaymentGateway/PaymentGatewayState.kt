package com.puyodev.luka.screens.PaymentGateway

sealed class PaymentGatewayState {
    sealed class PaymentStatus {
        object Idle : PaymentStatus()
        object Loading : PaymentStatus()
        object Processing : PaymentStatus()
        object Verifying : PaymentStatus()
        data class Success(val transactionId: String) : PaymentStatus()
        data class Error(val message: String) : PaymentStatus()
        object Cancelled : PaymentStatus()
    }
}