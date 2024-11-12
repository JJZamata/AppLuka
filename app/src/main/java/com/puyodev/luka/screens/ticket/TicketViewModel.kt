package com.puyodev.luka.screens.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.puyodev.luka.PAY_SCREEN
import com.puyodev.luka.model.service.ConfigurationService
import com.puyodev.luka.model.service.LogService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.screens.LukaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val state: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : LukaViewModel(logService) {

    val user = storageService.currentUserData

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
