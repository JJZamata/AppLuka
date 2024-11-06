package com.puyodev.luka.screens.operation

import androidx.compose.runtime.mutableStateOf
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.TICKET_SCREEN
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OperationsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : LukaViewModel(logService) {
    val options = mutableStateOf<List<String>>(listOf())

    val operations = storageService.operations

    val user = storageService.currentUserData

    fun onProfileClick(openScreen: (String) -> Unit) = openScreen(PROFILE_SCREEN)
    //fun onTicketClick(openScreen: (String) -> Unit) = openScreen(TICKET_SCREEN)

    fun loadOperationOptions() {
        //TODO
    }

}