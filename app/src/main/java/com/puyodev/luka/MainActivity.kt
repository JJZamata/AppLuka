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
import androidx.activity.compose.setContent
import com.puyodev.luka.screens.pay.PayViewModel

// Agrega esta anotación
@AndroidEntryPoint
class MainActivity : ComponentActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el adaptador NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        /*
        // Comprobar si NFC está habilitado
        if (nfcAdapter == null) {
            showAlertDialog("Error", "NFC no está disponible en este dispositivo.")
            return
        } else if (!nfcAdapter!!.isEnabled) {
            showAlertDialog("Advertencia", "Por favor habilita NFC en la configuración.")
        }
        */

        enableEdgeToEdge()

        setContent { LukaApp() }
    }

    override fun onResume() {
        super.onResume()
        // Habilitar el modo reader cuando la app está en primer plano
        nfcAdapter?.enableReaderMode(this, this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V,
            null)
    }

    override fun onPause() {
        super.onPause()
        // Deshabilitar el modo reader
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        // Notificar al ViewModel que se detectó una interacción NFC
        PayViewModel.onNFCDetected()
    }

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
