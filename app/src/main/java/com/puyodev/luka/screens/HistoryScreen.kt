package com.puyodev.luka.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// variables que define Operacion
data class Operacion(val operacion: String, val fecha: String, val monto: String)

val historial = listOf(
    Operacion("Recarga: Yape", "01/10/2024", "S/ 50.00"),
    Operacion("Pago de Pasaje", "05/10/2024", "S/ 1.00"),
    Operacion("Pago de Pasaje", "07/10/2024", "S/ 1.00"),
    Operacion("Pago de Pasaje", "07/10/2024", "S/ 5.00"),
    Operacion("Pago de Pasaje", "07/10/2024", "S/ 3.00"),
    Operacion("Pago de Pasaje", "07/10/2024", "S/ 2.00"),
    Operacion("Pago de Pasaje", "07/10/2024", "S/ 2.00"),
    Operacion("Recarga: Yape", "10/10/2024", "S/ 20.00")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialView(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
/*
    Scaffold(
        topBar = {
            CustomTopBar(navController = navController, name = "Juan", onMenuClick = {
                scope.launch { drawerState.open() }
            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(historial) { operacion ->
                OperacionItem(operacion)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    */
}

@Composable
fun OperacionItem(operacion: Operacion) {
    // Definir el color del monto basado en el tipo de operación
    val colorMonto = if (operacion.operacion.startsWith("Recarga")) {
        Color(0xFF4CAF50) // Verde para recargas
    } else {
        Color(0xFFF44336) // Rojo para pagos
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = operacion.operacion, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = operacion.fecha, style = MaterialTheme.typography.bodyMedium)
            }

            Text(
                text = operacion.monto,
                style = MaterialTheme.typography.bodyMedium,
                color = colorMonto,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialViewPreview() {
    // Aquí podrías crear una versión de prueba de la UI para verificar cómo se ve
}