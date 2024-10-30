package com.puyodev.luka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.puyodev.luka.navigation.AppNavigation
import com.puyodev.luka.ui.theme.LukaTheme
import dagger.hilt.android.AndroidEntryPoint
import android.nfc.NfcAdapter

// Agrega esta anotación
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        enableEdgeToEdge()
        setContent { LukaApp() }
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
