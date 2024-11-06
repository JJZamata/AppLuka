package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId

data class Operation(
    @DocumentId val id: String = "",
    val title: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val mount: String = "",
    val type: String = "",
    val userId: String = ""
)