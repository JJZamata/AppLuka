package com.puyodev.luka.screens

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
//pantalla para pruebas acerca de la activacion de NFC
@Composable
fun NFCActivationScreen(activity: Activity, nfcAdapter: NfcAdapter) {
    var nfcEnabled by remember { mutableStateOf(nfcAdapter.isEnabled) }
    var nfcChecked by remember { mutableStateOf(false) }

    // Layout
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (!nfcEnabled && !nfcChecked) {
            // If NFC is disabled, show a button to enable it
            Text(text = "NFC is disabled. Please enable it.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                activity.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                nfcChecked = true // Avoid showing the prompt again until checked
            }) {
                Text(text = "Enable NFC")
            }
        } else if (nfcChecked) {
            // After returning from NFC settings, check if NFC is enabled
            LaunchedEffect(Unit) {
                nfcEnabled = nfcAdapter.isEnabled
                nfcChecked = false // Reset the check after it is done
            }
        } else {
            // NFC is enabled, show confirmation
            Text(text = "NFC is enabled and ready to use!")
        }
    }
}
