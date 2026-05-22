package com.puyodev.luka.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

//Clase para manejar por separado las transacciones
data class PaymentOperation(
    @DocumentId val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val lukitasAmount: Int = 0,
    val paymentMethod: String = "PayPal",
    val transactionId: String = "",
    val status: String = "pending",
    val timestamp: Timestamp = Timestamp.now()
)
