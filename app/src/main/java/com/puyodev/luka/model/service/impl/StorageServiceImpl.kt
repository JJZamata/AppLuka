package com.puyodev.luka.model.service.impl

import android.util.Log
import com.puyodev.luka.model.User
import com.puyodev.luka.model.service.AccountService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.puyodev.luka.model.Operation
import com.puyodev.luka.model.PaymentOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.withContext

class StorageServiceImpl @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val auth: AccountService
) : StorageService {

  override val currentUserId: String
    get() = auth.currentUserId


  @OptIn(ExperimentalCoroutinesApi::class)
  override val currentUserData: Flow<User>
    get() = flow {
      val userId = auth.currentUserId
      if (userId.isNotEmpty()) {
        val documentSnapshot = firestore.collection(USER_COLLECTION)
          .document(userId)
          .get()
          .await()
        val userData = documentSnapshot.toObject(User::class.java)
        if (userData != null) {
          emit(userData)
        }
      }
    }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val operations: Flow<List<Operation>>
    get() = auth.currentUser.flatMapLatest { user ->
      firestore.collection(OPERATION_COLLECTION)
        .whereEqualTo(USER_ID_FIELD, user.id)
        .dataObjects()
    }

  override suspend fun getUser(userId: String): User? =
    firestore.collection(USER_COLLECTION).document(userId).get().await().toObject()

  override suspend fun updateUser(user: User) {
    firestore.collection(USER_COLLECTION).document(user.id).set(user).await()
  }

  override suspend fun savePaymentOperation(paymentOperation: PaymentOperation): String =
    withContext(Dispatchers.IO) {
      try {
        firestore.runTransaction { transaction ->
          // 1. Obtenemos la referencia del usuario y verificamos que existe
          val userRef = firestore.collection(USER_COLLECTION).document(currentUserId)
          val userSnapshot = transaction.get(userRef)

          if (!userSnapshot.exists()) {
            Log.e("Storage", "Usuario no encontrado: $currentUserId")
            throw Exception("Usuario no encontrado")
          }

          // 2. Preparamos la referencia para la nueva operación
          val paymentRef = firestore.collection(PAYMENT_OPERATIONS_COLLECTION).document()

          // 3. Calculamos el nuevo balance de lukitas
          val currentLukitas = userSnapshot.getLong("lukitas")?.toInt() ?: 0
          Log.d("Storage", "Lukitas actuales: $currentLukitas")
          val newLukitas = currentLukitas + paymentOperation.lukitasAmount
          Log.d("Storage", "Nuevo balance de lukitas: $newLukitas")

          // 4. Creamos la operación con el ID generado
          val finalOperation = paymentOperation.copy(
            id = paymentRef.id,
            status = "completed" // Actualizamos el estado a completed
          )

          // 5. Ejecutamos ambas operaciones en la transacción
          transaction.set(paymentRef, finalOperation)
          transaction.update(userRef, "lukitas", newLukitas)

          paymentRef.id
        }.await()
      } catch (e: Exception) {
        Log.e("Storage", "Error en savePaymentOperation: ${e.message}")
        throw Exception("Error en la transacción de pago: ${e.message}")
      }
    }

  override suspend fun updateUserLukitas(userId: String, newLukitasAmount: Int): Boolean =
    withContext(Dispatchers.IO) {
      try {
        firestore.runTransaction { transaction ->
          val userRef = firestore.collection(USER_COLLECTION).document(userId)
          val userSnapshot = transaction.get(userRef)

          if (!userSnapshot.exists()) {
            throw Exception("Usuario no encontrado")
          }

          transaction.update(userRef, "lukitas", newLukitasAmount)
        }.await()
        true
      } catch (e: Exception) {
        throw Exception("Error al actualizar Lukitas: ${e.message}")
      }
    }

  override suspend fun getPaymentOperations(userId: String): List<PaymentOperation> =
    withContext(Dispatchers.IO) {
      try {
        firestore.collection(PAYMENT_OPERATIONS_COLLECTION)
          .whereEqualTo("userId", userId)
          .orderBy("timestamp", Query.Direction.DESCENDING)
          .get()
          .await()
          .toObjects(PaymentOperation::class.java)
      } catch (e: Exception) {
        throw Exception("Error al obtener el historial de pagos: ${e.message}")
      }
    }

  override suspend fun getCurrentLukitasBalance(userId: String): Int =
    withContext(Dispatchers.IO) {
      try {
        val userDoc = firestore.collection(USER_COLLECTION)
          .document(userId)
          .get()
          .await()

        userDoc.getLong("lukitas")?.toInt() ?: 0
      } catch (e: Exception) {
        throw Exception("Error al obtener el balance de Lukitas: ${e.message}")
      }
    }

  override suspend fun getOperation(operationId: String): Operation? {
    try {
      val documentSnapshot = firestore.collection(OPERATION_COLLECTION)
        .document(operationId)
        .get()
        .await()

      Log.d("StorageService", "Si se existe: ${documentSnapshot.exists()}")
      if (documentSnapshot.exists()) {
        val operation = documentSnapshot.toObject(Operation::class.java)
        Log.d("StorageService", "Operacion obtenida: $operation")
        return operation
      } else {
        Log.e("StorageService", "No se encontro un documento con tal id: $operationId")
        return null
      }
    } catch (e: Exception) {
      Log.e("StorageService", "Error retrieving operation", e)
      return null
    }
  }
  override suspend fun getOperationFlow(operationId: String): Flow<Operation> = callbackFlow {
    val subscription = firestore.collection(OPERATION_COLLECTION)
      .document(operationId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }

        snapshot?.toObject(Operation::class.java)?.let { operation ->
          trySend(operation)
        }
      }

    awaitClose { subscription.remove() }
  }

  override suspend fun save(operation: Operation): String =
      trace(SAVE_OPERATION_TRACE) {
        val operationWithUserId = operation.copy(userId = auth.currentUserId)
        firestore.collection(OPERATION_COLLECTION).add(operationWithUserId).await().id
      }

  override suspend fun update(operation: Operation): Unit =
      trace(UPDATE_OPERATION_TRACE) {
        firestore.collection(OPERATION_COLLECTION).document(operation.id).set(operation).await()
      }


  override suspend fun delete(operationId: String) {
    firestore.collection(OPERATION_COLLECTION).document(operationId).delete().await()
  }

  companion object {
    private const val USER_COLLECTION = "usuarios"

    private const val USER_ID_FIELD = "userId"
    private const val OPERATION_COLLECTION = "operations"
    private const val PAYMENT_OPERATIONS_COLLECTION = "payment_operations"
    private const val SAVE_OPERATION_TRACE = "saveOperation"
    private const val UPDATE_OPERATION_TRACE = "updateOperation"
  }
}