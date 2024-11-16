package com.puyodev.luka.screens.PaymentGateway

import com.puyodev.luka.PAYMENT_SCREEN
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentGatewayViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : LukaViewModel(logService) {
    val user = storageService.currentUserData

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)

}