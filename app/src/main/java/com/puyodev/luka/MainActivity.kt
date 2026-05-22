package com.puyodev.luka

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.puyodev.luka.ui.theme.LukaTheme
import dagger.hilt.android.AndroidEntryPoint

import android.app.AlertDialog
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.puyodev.luka.screens.PaymentGateway.PayPalConfig
import com.puyodev.luka.screens.pay.PayViewModel
import javax.inject.Inject

// Agrega esta anotación
@AndroidEntryPoint
class MainActivity : ComponentActivity()/*, NfcAdapter.ReaderCallback*/ {

    //private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el adaptador NFC
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Comprobar si NFC está disponible y habilitado
        //when {
          //  nfcAdapter == null -> {
            //    showNFCError("NFC no está disponible en este dispositivo.")
              //  return
            //}
            //!nfcAdapter!!.isEnabled -> {
             //   showNFCSettings("Por favor habilita NFC para usar la aplicación.")
            //}
        //}

        enableEdgeToEdge()

        setContent {
            LukaApp()
        }
    }

/*
    override fun onResume() {
        super.onResume()
        try {
            nfcAdapter?.enableReaderMode(
                this,
                this,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_NFC_F or
                        NfcAdapter.FLAG_READER_NFC_V or
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                        NfcAdapter.FLAG_READER_NFC_BARCODE, // Agregar esta flag
                Bundle().apply {
                    // Reducir el delay podría ayudar
                    putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
                }
            )
        } catch (e: Exception) {
            Log.e("NFC", "Error al habilitar el modo reader: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        // Deshabilitar el modo reader
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        try {
            tag?.let {
                // Intentar obtener todas las tecnologías disponibles
                val techs = tag.techList
                Log.d("NFC", "Tecnologías disponibles: ${techs.joinToString()}")

                // Notificar al ViewModel
                PayViewModel.onNFCDetected()
            }
        } catch (e: Exception) {
            Log.e("NFC", "Error en onTagDiscovered: ${e.message}")
        }
    }

    private fun showNFCError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun showNFCSettings(message: String) {
        AlertDialog.Builder(this)
            .setTitle("NFC Deshabilitado")
            .setMessage(message)
            .setPositiveButton("Ir a Configuración") { _, _ ->
                startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
*/
    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        LukaTheme {
            Surface {
                content()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApp {
            Text("Hello NFC!")
        }
    }
}