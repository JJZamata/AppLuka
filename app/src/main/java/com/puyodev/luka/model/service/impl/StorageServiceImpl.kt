package com.puyodev.luka.model.service.impl

import com.puyodev.luka.model.User
import com.puyodev.luka.model.service.AccountService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.puyodev.luka.model.Operation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageServiceImpl @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val auth: AccountService
) : StorageService {

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

  override suspend fun getUser(userId: String): User? =
    firestore.collection(USER_COLLECTION).document(userId).get().await().toObject()

  override suspend fun updateUser(user: User) {
    firestore.collection(USER_COLLECTION).document(user.id).set(user).await()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val operations: Flow<List<Operation>>
    get() =
      auth.currentUser.flatMapLatest { user ->
        firestore.collection(OPERATION_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
      }

  override suspend fun getOperation(operationId: String): Operation? =
    firestore.collection(OPERATION_COLLECTION).document(operationId).get().await().toObject()

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
    private const val SAVE_OPERATION_TRACE = "saveOperation"
    private const val UPDATE_OPERATION_TRACE = "updateOperation"
  }
}