package com.puyodev.luka.screens.pay

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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

                // Crear nueva operación
                val newOperation = Operation(
                    from = "101",
                    mount = valor.toString(),
                    type = "Pago",
                    busStop = direccion,
                    uid = "",  // Será llenado por el Raspberry Pi
                    timestamp = Timestamp.now(),
                    status = "pending"
                    // completedTimestamp se llenará cuando el Raspberry Pi actualice la operación
                )

                // Guardar en Firestore
                val operationId = storageService.save(newOperation)

                // Observa cambios en la operación por 8 segundos
                val timeoutMillis = 8000L // Tiempo de espera
                val flow = storageService.getOperationFlow(operationId)

                withTimeoutOrNull(timeoutMillis) {
                    flow.collect { operation ->
                        if (operation.uid.isNotEmpty()) {
                            _nfcStatus.value = NFCStatus.Success
                            val route = "$TICKET_SCREEN/$valor/$direccion"
                            openScreen(route)
                            return@collect
                        }
                    }
                }

                // Si el tiempo se agota sin éxito, muestra error
                if (_nfcStatus.value != NFCStatus.Success) {
                    _nfcStatus.value = NFCStatus.Error("No se detectó respuesta del lector NFC.")
                    SnackbarManager.showMessage(AppText.nfc_timeout)
                }

            } catch (e: Exception) {
                // Maneja cualquier error inesperado
                _nfcStatus.value = NFCStatus.Error(e.message ?: "Error desconocido")
                SnackbarManager.showMessage(AppText.nfc_error)
                Log.e("NFC_ERROR", "${AppText.nfc_error} Error: ${e.message}")
            } finally {
            delay(2000) // Espera 3 segundos para mostrar el mensaje
            _nfcStatus.value = NFCStatus.Idle // Restablecer estado
        }
        }
    }
}