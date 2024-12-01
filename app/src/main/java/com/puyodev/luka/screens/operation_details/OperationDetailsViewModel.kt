package com.puyodev.luka.screens.operation_details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import com.puyodev.luka.OPERATION_ID
import com.puyodev.luka.PAY_SCREEN
import com.puyodev.luka.PROFILE_SCREEN
import com.puyodev.luka.common.ext.idFromParameter
import com.puyodev.luka.model.Operation
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import android.util.Log
import com.puyodev.luka.R
import javax.inject.Inject

@HiltViewModel
class OperationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
) : LukaViewModel(logService) {
    val operation = mutableStateOf(Operation())

    init {
        val operationId = savedStateHandle.get<String>(OPERATION_ID)
        if (operationId == null) {
            Log.e("prueba", "Operation ID is null.")
        } else {
            Log.e("prueba", "Operation ID: $operationId")
            launchCatching {
                operation.value = storageService.getOperation(operationId.idFromParameter()) ?: Operation()
                Log.e("prueba", operation.value.toString())

            }
        }
    }

    fun onPayScreenClick(openScreen: (String) -> Unit) = openScreen(PAY_SCREEN)

    // Función para capturar la imagen de la pantalla y devolver su URI
    fun captureAndShareImage(view: View, context: Context): Uri? {
        return try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            view.draw(canvas)

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ticket_${UUID.randomUUID()}.png")
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Función para compartir la imagen usando su URI
    fun shareImage(context: Context, imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir Ticket"))
    }

}

