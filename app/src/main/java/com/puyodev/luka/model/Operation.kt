// Operation.kt
package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp

data class Operation(
    @DocumentId val id: String = "",
    val from: String = "",
    val mount: String = "",
    val type: String = "",
    val busStop: String = "",
    val uid: String = "",     // Este campo será llenado por el Raspberry Pi
    val userId: String = "",
    val timestamp: Timestamp? = null,
    val status: String = "pending",    // Nuevo campo para control de estado
    val completedTimestamp: Timestamp? = null  // Nuevo campo para timestamp de completado
)