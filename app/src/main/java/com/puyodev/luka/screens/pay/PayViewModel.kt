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
        _isLoading.value = true
        val token = generateToken()

        operation.value = operation.value.copy(
            from = "101",
            createdDate = createdDate.toString(),
            createdTime = createdTime,
            mount = valor.toString(),
            type = "Pago",
            busStop = direccion,
            uidTag = "",
            token = token // Añadir el token aquí
        )

        launchCatching{
            val editedOperation = operation.value
            if (editedOperation.id.isBlank()) {
                storageService.save(editedOperation)
            } else {
                storageService.update(editedOperation)
            }

            delay(1000)

            val route = "$TICKET_SCREEN/$valor/$direccion" // Enviar resultados por parámetro
            openScreen(route)
            /*
            // Verifica si `uidTag` está lleno después del retraso
            if (operation.value.uidTag.isNotEmpty() && operation.value.uidTag != "") {
                // Si `uidTag` tiene un valor válido, navega a TICKET_SCREEN
                val route = "$TICKET_SCREEN/$valor/$direccion" // Enviar resultados por parámetro
                openScreen(route)
            } else {
                // Si `uidTag` está vacío o aún es "pendiente", muestra el mensaje de error
                SnackbarManager.showMessage(AppText.failed)
            }
*/

            _isLoading.value = false
        }
    }
}


// Función para generar un token seguro
private fun generateToken(): String {
    // Aquí puedes utilizar una función para generar un token aleatorio seguro, como UUID
    return java.util.UUID.randomUUID().toString()
}