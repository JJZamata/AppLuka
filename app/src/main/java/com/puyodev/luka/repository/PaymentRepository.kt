package com.puyodev.luka.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PaymentRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun recordPayment(userId: String, amount: Double): Result<String> {
        return try {
            val paymentData = mapOf(
                "userId" to userId,
                "amount" to amount,
                "timestamp" to FieldValue.serverTimestamp()
            )
            db.collection("payments").add(paymentData).await()
            Result.success("Payment recorded successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
