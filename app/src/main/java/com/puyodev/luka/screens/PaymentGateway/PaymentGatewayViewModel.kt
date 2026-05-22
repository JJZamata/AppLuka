package com.puyodev.luka.screens.PaymentGateway

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.model.PaymentOperation
import com.puyodev.luka.model.User
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PaymentGatewayViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
    // PayPalConfig deshabilitado para demo
    // private val payPalConfig: PayPalConfig
) : LukaViewModel(logService) {

    // Nuevo: Job para rastrear operaciones en curso
    private var currentPaymentJob: Job? = null

    private var _currentUserId: String? = null


    // Observamos el usuario actual usando el Flow proporcionado por StorageService
    val user = storageService.currentUserData
        .onEach { user ->
            _currentUserId = user.id
            logService.logMessage("Usuario actual ID: ${user.id}")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )


    // Estados de pago
    private val _paymentStatus = MutableStateFlow<PaymentGatewayState.PaymentStatus>(
        PaymentGatewayState.PaymentStatus.Idle
    )
    val paymentStatus = _paymentStatus.asStateFlow()

    // Monto seleccionado para la recarga
    private val _selectedAmount = MutableStateFlow(0)
    val selectedAmount = _selectedAmount.asStateFlow()

    init {
        viewModelScope.launch {
            logService.logMessage("Inicializando ViewModel - Modo DEMO (sin PayPal)")
        }
        initializePaymentSystem()
    }

    private fun initializePaymentSystem() {
        viewModelScope.launch {
            logService.logMessage("Sistema de pago en modo DEMO - PayPal deshabilitado")
        }
    }

    // Función vacía para compatibilidad (PayPal deshabilitado)
    private fun setupPayPalCallbacks() {
        // NO-OP en modo demo
    }

    private fun handlePayPalApproval() {
        // NO-OP en modo demo
    }

    fun processPayment(amount: Int) {
        viewModelScope.launch {
            try {
                // Validate the amount first
                if (amount <= 0) {
                    _paymentStatus.value = PaymentGatewayState.PaymentStatus.Error("El monto debe ser mayor a 0")
                    return@launch
                }

                // Store the selected amount and update status to Loading
                _selectedAmount.value = amount
                _paymentStatus.value = PaymentGatewayState.PaymentStatus.Loading

                logService.logMessage("Iniciando proceso de pago DEMO por $amount lukitas")

                // Simular proceso de pago con delay
                kotlinx.coroutines.delay(2000) // 2 segundos de "procesamiento"

                // Generar un ID de transacción falso para la demo
                val fakeTransactionId = "DEMO-${System.currentTimeMillis()}"

                // Procesar pago exitoso simulado
                processCaptureSuccess(fakeTransactionId)

            } catch (e: Exception) {
                logService.logError("Error al iniciar el pago", e)
                _paymentStatus.value = PaymentGatewayState.PaymentStatus.Error(
                    "Error al iniciar el pago: ${e.message}"
                )
            }
        }
    }

    private suspend fun processCaptureSuccess(orderId: String) {
        try {
            logService.logMessage("Procesando captura exitosa: $orderId")
            val lukitasAmount = _selectedAmount.value

            if (lukitasAmount <= 0) {
                throw Exception("Monto inválido")
            }

            val paymentOperation = PaymentOperation(
                userId = storageService.currentUserId,
                amount = lukitasAmount.toDouble(),
                lukitasAmount = lukitasAmount,
                paymentMethod = "DEMO",
                transactionId = orderId,
                status = "completed",
                timestamp = Timestamp.now()
            )

            withContext(Dispatchers.IO) {
                val operationId = storageService.savePaymentOperation(paymentOperation)
                logService.logMessage("Operación guardada exitosamente: $operationId")
                _paymentStatus.value = PaymentGatewayState.PaymentStatus.Success(orderId)
            }
        } catch (e: Exception) {
            handlePaymentError(e)
        }
    }


    private suspend fun isPaymentAlreadyProcessed(orderId: String): Boolean {
        return try {
            // Verificar en Firestore si el pago ya existe
            val existingPayments = storageService.getPaymentOperations(storageService.currentUserId)
            existingPayments.any { it.transactionId == orderId }
        } catch (e: Exception) {
            logService.logError("Error al verificar pago existente", e)
            false
        }
    }

    private fun handlePayPalCancellation() {
        viewModelScope.launch {
            logService.logMessage("Pago cancelado por el usuario (DEMO)")
            _paymentStatus.value = PaymentGatewayState.PaymentStatus.Cancelled
            resetPaymentStatus()
        }
    }

    // Funciones vacías para compatibilidad en modo demo
    private fun handlePayPalError(errorInfo: Any) {
        // NO-OP en modo demo
    }

    private fun handleCaptureError(captureResult: Any) {
        // NO-OP en modo demo
    }

    private fun handlePaymentError(error: Exception) {
        viewModelScope.launch {
            logService.logError("Error en el proceso de pago", error)
            _paymentStatus.value = PaymentGatewayState.PaymentStatus.Error(
                error.message ?: "Error desconocido")
        }
    }
    fun resetPaymentStatus() {
        viewModelScope.launch {
            _paymentStatus.value = PaymentGatewayState.PaymentStatus.Idle
            _selectedAmount.value = 0
        }
    }
    override fun onCleared() {
        super.onCleared()
        // PayPal cleanup deshabilitado en modo demo
        // payPalConfig.cleanup()
    }

    fun cancelPayment() {
        viewModelScope.launch {
            // Cancel any ongoing operations
            _paymentStatus.value = PaymentGatewayState.PaymentStatus.Idle
            // Add any cleanup logic needed
        }

    }

    companion object {
        const val LUKITA_TO_USD_RATE = 1.0 // 1 Lukita = 1 USD
    }

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)

}