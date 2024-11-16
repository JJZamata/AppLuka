// Operation.kt
package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp

data class Operation(
    @DocumentId val id: String = "",
    val from: String = "",
    val createdDate: String = "",
    val createdTime: String = "",
    val mount: String = "",
    val type: String = "",
    val busStop: String = "",
    val uid: String = "",     // Este campo será llenado por el Raspberry Pi
    val userId: String = "",
    val timestamp: Timestamp? = null
)