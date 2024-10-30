package com.puyodev.luka.model.service.impl

import com.puyodev.luka.model.User
import com.puyodev.luka.model.service.AccountService
//import com.puyodev.luka.model.service.trace sirve para medir el rendimiento de un bloque de código con Firebase Performance Monitoring.
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : AccountService {

  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  override val hasUser: Boolean
    get() = auth.currentUser != null

  override val currentUser: Flow<User>
    get() = callbackFlow {
      val listener =
        FirebaseAuth.AuthStateListener { auth ->
          this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
        }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  // Crear una nueva cuenta
  override suspend fun createAccount(email: String, password: String, name:String) {
    val result = auth.createUserWithEmailAndPassword(email, password).await()

    // Obtener el UID del usuario creado
    val uid = result.user?.uid ?: throw Exception("Error creando usuario")

    // Crear el documento en Firestore con datos adicionales
    val userData = hashMapOf(
      "username" to name,
      "lukitas" to 0.00 // Monto inicial
    )
    firestore.collection("usuarios").document(uid).set(userData).await()

    auth.signInWithEmailAndPassword(email, password).await()
  }

  // Iniciar sesión con usuario existente
  override suspend fun authenticate(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun deleteAccount() {
    val user = auth.currentUser
    if (user != null) {
      user.delete().await()
    } else {
      throw Exception("No se encontró el usuario actual para eliminar")
    }
  }

  override suspend fun signOut() {
    if (auth.currentUser!!.isAnonymous) {
      auth.currentUser!!.delete()
    }
    auth.signOut()
  }
}