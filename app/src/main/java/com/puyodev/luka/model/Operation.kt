package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId

data class Operation(
    @DocumentId val id: String = "",
    val title: String = "",
    val createdDate: String = "",
    val createdTime: String = "",
    val mount: String = "",
    val type: String = "",
    val userId: String = ""
)