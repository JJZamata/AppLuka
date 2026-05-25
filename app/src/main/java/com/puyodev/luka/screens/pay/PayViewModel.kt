package com.puyodev.luka.screens.pay

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.puyodev.luka.OPERATION_ID
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.TICKET_SCREEN
import com.puyodev.luka.common.ext.idFromParameter
import com.puyodev.luka.model.Operation
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.google.firebase.Timestamp
import com.puyodev.luka.PAYMENT_SCREEN
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.model.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import com.puyodev.luka.R.string as AppText

@HiltViewModel
class PayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService,
) : LukaViewModel(logService) {

    val user = storageService.currentUserData
    val operation = mutableStateOf(Operation())


    init {
        val operationId = savedStateHandle.get<String>(OPERATION_ID)
        if (operationId != null) {
            launchCatching {
                operation.value = storageService.getOperation(operationId.idFromParameter()) ?: Operation()
            }
        }
    }

    private val _nfcStatus = MutableStateFlow<NFCStatus>(NFCStatus.Idle)
    val nfcStatus = _nfcStatus.asStateFlow()

    sealed class NFCStatus {
        object Idle : NFCStatus()
        object WaitingForNFC : NFCStatus()
        object Success : NFCStatus()
        data class Error(val message: String) : NFCStatus()
    }
    companion object {
        private val _nfcDetected = MutableStateFlow(false)

        fun onNFCDetected() {
            _nfcDetected.value = true
        }
    }
    //logica para la navegacion a la pantalla recargar lukitas
    fun onProfilePaymentGatewayClick(openScreen: (String) -> Unit) = openScreen(PAYMENT_SCREEN)

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)

    fun onTicketClick(openScreen: (String) -> Unit, valor: Int, direccion: String) {
        viewModelScope.launch {
            try {
                _nfcStatus.value = NFCStatus.WaitingForNFC

                // Obtener el usuario actual de forma síncrona
                val currentUser = storageService.getUser(storageService.currentUserId)
                val currentLukitas = currentUser?.lukitas ?: 0

                if (currentLukitas < valor) {
                    _nfcStatus.value = NFCStatus.Error("No tienes suficientes lukitas")
                    SnackbarManager.showMessage(com.puyodev.luka.common.snackbar.SnackbarMessage.StringSnackbar("Saldo insuficiente"))
                    delay(2000)
                    _nfcStatus.value = NFCStatus.Idle
                    return@launch
                }

                // Crear nueva operación con userId incluido
                val newOperation = Operation(
                    from = "101",
                    mount = valor.toString(),
                    type = "Pago",
                    busStop = direccion,
                    uid = "",  // Será llenado por simulación DEMO
                    userId = storageService.currentUserId, // Incluir userId del usuario actual
                    timestamp = Timestamp.now(),
                    status = "pending"
                )

                // Guardar en Firestore
                val operationId = storageService.save(newOperation)

                // MODO DEMO: Simular respuesta del lector NFC después de 3 segundos
                delay(3000L)

                // Obtener la operación y actualizarla con uid falso de DEMO
                val updatedOperation = newOperation.copy(
                    id = operationId,
                    uid = "DEMO-READER-${System.currentTimeMillis()}",
                    status = "completed",
                    completedTimestamp = Timestamp.now() // Establecer timestamp de completado
                )
                storageService.update(updatedOperation)

                // Restar lukitas al usuario
                val newLukitas = currentLukitas - valor
                storageService.updateUserLukitas(storageService.currentUserId, newLukitas)

                // Esperar un momento más para que se actualice
                delay(500L)

                _nfcStatus.value = NFCStatus.Success
                val route = "$TICKET_SCREEN/$valor/$direccion" // Volver al formato original
                openScreen(route)

            } catch (e: Exception) {
                // Maneja cualquier error inesperado
                _nfcStatus.value = NFCStatus.Error(e.message ?: "Error desconocido")
                SnackbarManager.showMessage(AppText.nfc_error)
                Log.e("NFC_ERROR", "${AppText.nfc_error} Error: ${e.message}")
            } finally {
                delay(2000)
                _nfcStatus.value = NFCStatus.Idle
            }
        }
    }
}