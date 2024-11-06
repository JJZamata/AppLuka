package com.puyodev.luka.model.service

import com.puyodev.luka.model.Operation
import com.puyodev.luka.model.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val operations: Flow<List<Operation>>
  suspend fun getOperation(operationId: String): Operation?
  suspend fun save(operation: Operation): String
  suspend fun update(operation: Operation)
  suspend fun delete(operationId: String)

  val currentUserData: Flow<User>
  suspend fun getUser(userId: String): User?
  suspend fun updateUser(user: User)
}
