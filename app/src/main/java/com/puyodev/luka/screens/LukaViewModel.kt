package com.puyodev.luka.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puyodev.luka.common.snackbar.SnackbarManager
import com.puyodev.luka.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.puyodev.luka.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class LukaViewModel(val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}