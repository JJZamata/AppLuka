package com.puyodev.luka.screens.pay

import android.os.Build
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.R.string as AppText

@HiltViewModel
class PayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService,
) : LukaViewModel(logService) {

    val user = storageService.currentUserData
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
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
    //Hora y fecha peruana
    // Configurar zona horaria de Perú (UTC-5)
    @RequiresApi(Build.VERSION_CODES.O)
    val peruZone = ZoneId.of("America/Lima")

    // Asignar fecha actual
    @RequiresApi(Build.VERSION_CODES.O)
    val createdDate = LocalDate.now(peruZone)

    // Asignar hora actual formateada a "HH:mm" (horas y minutos)
    @RequiresApi(Build.VERSION_CODES.O)
    val createdTime: String = LocalTime.now(peruZone).format(DateTimeFormatter.ofPattern("HH:mm"))
    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)
    //fun onTicketClick(openScreen: (String) -> Unit) = openScreen(TICKET_SCREEN)
    @RequiresApi(Build.VERSION_CODES.O)
    fun onTicketClick(openScreen: (String) -> Unit, valor: Int, direccion: String) {
        viewModelScope.launch {
            try {
                _nfcStatus.value = NFCStatus.WaitingForNFC
                _isLoading.value = true

                // Crear nueva operación
                val newOperation = Operation(
                    from = "101",
                    createdDate = createdDate.toString(),
                    createdTime = createdTime,
                    mount = valor.toString(),
                    type = "Pago",
                    busStop = direccion,
                    uid = "",  // Será llenado por el Raspberry Pi
                    timestamp = Timestamp.now()
                )

                // Guardar en Firestore
                storageService.save(newOperation)

                // Navegar inmediatamente a la pantalla de ticket
                _nfcStatus.value = NFCStatus.Success
                val route = "$TICKET_SCREEN/$valor/$direccion"
                openScreen(route)

            } catch (e: Exception) {
                _nfcStatus.value = NFCStatus.Error(e.message ?: "Error desconocido")
            } finally {
                _isLoading.value = false
            }
        }
    }
}