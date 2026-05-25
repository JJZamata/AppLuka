package com.puyodev.luka.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val userId: String = "",       // Campo necesario para las reglas de Firestore
    val username: String = "",
    val lukitas: Int = 0
)