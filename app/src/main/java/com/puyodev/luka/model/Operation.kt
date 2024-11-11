package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId

data class Operation(
    @DocumentId val id: String = "",
    val from: String = "",
    val createdDate: String = "",
    val createdTime: String = "",
    val mount: String = "",
    val type: String = "",
    val busStop: String = "",
    val uidTag: String = "",
    val token: String = "",
    val userId: String = "",
)