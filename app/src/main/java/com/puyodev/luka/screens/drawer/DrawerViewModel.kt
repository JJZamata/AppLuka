package com.puyodev.luka.screens.drawer

import androidx.compose.runtime.mutableStateOf
import com.puyodev.luka.INFO_SCREEN
import com.puyodev.luka.OPERATIONS_SCREEN
import com.puyodev.luka.PAYMENT_SCREEN
import com.puyodev.luka.PAY_SCREEN
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.TICKET_SCREEN
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
//import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : LukaViewModel(logService) {
    val user = storageService.currentUserData

    fun onPayScreenClick(openScreen: (String) -> Unit) = openScreen(PAY_SCREEN)
    fun onInfoScreenClick(openScreen: (String) -> Unit) = openScreen(INFO_SCREEN)
    //fun onNotificationScreenClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)
    fun onRechargeScreenClick(openScreen: (String) -> Unit) = openScreen(PAYMENT_SCREEN)
    fun onConfigurationScreenClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)
    fun onHistoryScreenClick(openScreen: (String) -> Unit) = openScreen(OPERATIONS_SCREEN)
}