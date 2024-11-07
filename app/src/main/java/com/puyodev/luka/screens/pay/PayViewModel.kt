package com.puyodev.luka.screens.pay

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.TICKET_SCREEN
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.repository.PaymentRepository
//import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService,
    private val paymentRepository: PaymentRepository
) : LukaViewModel(logService) {

    val user = storageService.currentUserData
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _paymentResult = MutableLiveData<Result<String>>() // Mantiene el estado de la operación
    val paymentResult: LiveData<Result<String>> = _paymentResult

    fun registerPayment(userId: String, amount: Double) {
        viewModelScope.launch {
            val result = paymentRepository.recordPayment(userId, amount)
            _paymentResult.value = result // Observamos el resultado para actualizar la UI
        }
    }

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)
    //fun onTicketClick(openScreen: (String) -> Unit) = openScreen(TICKET_SCREEN)
    fun onTicketClick(openScreen: (String) -> Unit, valor: Int, direccion: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(5000)
            _isLoading.value = false
            val route = "$TICKET_SCREEN/$valor/$direccion"
            openScreen(route)
        }
    }

}