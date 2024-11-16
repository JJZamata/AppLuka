package com.puyodev.luka.screens.pay

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puyodev.luka.R
import com.puyodev.luka.common.composable.ActionToolbar
import com.puyodev.luka.common.ext.toolbarActions
import com.puyodev.luka.model.User
import com.puyodev.luka.screens.drawer.DrawerHeader
import com.puyodev.luka.screens.drawer.DrawerScreen
import kotlinx.coroutines.delay
//import com.example.makeitso.model.Task
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction3

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayScreen(
    openScreen: (String) -> Unit,
    viewModel: PayViewModel = hiltViewModel(),
) {
    // Observa un único objeto User en lugar de una lista
    val user by viewModel.user.collectAsStateWithLifecycle(initialValue = User())
    val nfcStatus by viewModel.nfcStatus.collectAsState() // Añadido el estado NFC
    PayScreenContent(
        user = user,
        onProfileClick = viewModel::onProfileClick,
        onTicketClick = viewModel::onTicketClick,
        onProfilePaymentGatewayClick = viewModel::onProfilePaymentGatewayClick,
        openScreen = openScreen,
        nfcStatus = nfcStatus
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun PayScreenContent(
    user: User,
    onProfileClick: ((String) -> Unit) -> Unit,
    onProfilePaymentGatewayClick: ((String) -> Unit) -> Unit,
    onTicketClick: KFunction3<(String) -> Unit, Int, String, Unit>,
    openScreen: (String) -> Unit,
    nfcStatus: PayViewModel.NFCStatus
) {
    var valor by remember { mutableIntStateOf(1) } // Estado del contador
    val drawerState = rememberDrawerState(DrawerValue.Closed) // Estado para abrir/cerrar el drawer
    val scope = rememberCoroutineScope() // Alcance de la corrutina para manejar el drawer


    ModalNavigationDrawer(
            drawerState = drawerState, // Controla si el drawer está abierto o cerrado
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    DrawerHeader(user = user.username)
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio desde el borde superior
                    DrawerScreen(openScreen = openScreen) // Pasando el parámetro requerido
                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                topBar = {
                    ActionToolbar(
                        title = user.username,  // Muestra el nombre de usuario
                        modifier = Modifier.toolbarActions(),
                        //endActionIcon = AppIcon.ic_settings,
                        endAction = { onProfileClick(openScreen)},
                        onMenuClick = {
                            scope.launch { drawerState.open() } // Abre el drawer al hacer clic en el menú
                        }
                    )
                },
                bottomBar = { CustomBottomBar() }, // Agregar la barra inferior aquí
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los elementos de forma uniforme
                    ) {
                        OutlinedButton(
                            modifier = Modifier.width(160.dp),
                            onClick = { onProfilePaymentGatewayClick(openScreen)}
                        ) {
                            Column {
                                Text(
                                    text = "Lukitas: ",
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre el ícono y el texto
                                ) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.lukita_coin),
                                        contentDescription = "Lukitas Icono",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Unspecified

                                    )
                                    Text(
                                        text = "${user.lukitas}",
                                    )
                                }
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier.width(160.dp),
                            onClick = { /*TODO*/ }
                        ) {
                            Column {
                                Text(
                                    text = "Tarifa: ",
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre el ícono y el texto
                                ) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.lukita_coin),
                                        contentDescription = "Lukitas Icono",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Unspecified

                                    )
                                    Text(
                                        text = "1",
                                        //text = "${user.lukitas}",
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically // Centra verticalmente el contenido del Row
                        ) {
                            IconButton(onClick = {
                                if (valor > 1) valor-- // Resta 1 si valor es mayor que 0 - límite 1
                            }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Minus")
                            }
                            Text(text = "$valor", fontSize = 100.sp)

                            IconButton(onClick = {
                                if (valor < 10) valor++ // Suma 1 si valor es menor a 10 - límite 10
                            }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Plus")
                            }
                        }
                    }

                    // LazyVerticalGrid con GridCells.Adaptive para ajustar el tamaño de las imágenes
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 60.dp), // El tamaño mínimo que se ajusta según el espacio
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(valor) { // Repite la imagen tantas veces como el valor
                            Image(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f) // Mantiene el aspecto cuadrado
                                    .clip(RoundedCornerShape(8.dp)) // Opcional: redondear las esquinas
                                    .border(1.dp, Color.Gray) // Opcional: agregar un borde
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        when (nfcStatus) {
                            is PayViewModel.NFCStatus.Idle -> {
                                ExtendedFloatingActionButton(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .padding(0.dp, 40.dp),
                                    onClick = {
                                        onTicketClick(
                                            openScreen,
                                            valor,
                                            "Urb. Monterrey D-8, José Luis Bustamante y Rivero"
                                        )
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    icon = {
                                        Icon(
                                            Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Realizar pago"
                                        )
                                    },
                                    text = { Text(text = "Pagar", fontSize = 20.sp) }
                                )
                            }
                            is PayViewModel.NFCStatus.WaitingForNFC -> {
                                NFCWaitingIndicator()
                            }
                            is PayViewModel.NFCStatus.Success -> {
                                // No mostrar nada, la navegación ocurrirá automáticamente
                            }
                            is PayViewModel.NFCStatus.Error -> {
                                ErrorMessage(message = nfcStatus.message)
                            }
                        }
                    }
                }
            }
    }
}

@Composable
fun NFCWaitingIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Acerque su teléfono al lector...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CustomBottomBar() {
    BottomAppBar(
        modifier = Modifier
            .shadow(elevation = 10.dp)
            .height(150.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Asegura que las columnas queden separadas
        ) {
            Column(
                modifier = Modifier
                    .weight(1f) // Cada columna ocupa el mismo ancho
                    .padding(end = 8.dp) // Espacio opcional entre las columnas
            ) {
                Text(text = "Ubicación Actual:", fontSize = 15.sp)
                Text(
                    text = "Urb. Monterrey D-8, José Luis Bustamante y Rivero",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.45f) // Cada columna ocupa el mismo ancho
                    .padding(start = 8.dp) // Espacio opcional entre las columnas
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(), // Hace que el Box ocupe todo el espacio de la columna
                    contentAlignment = Alignment.Center // Centra la imagen dentro del Box
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.located),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp) // Tamaño personalizado para la imagen centrada
                    )
                }
            }
        }
    }
}

//enviar parametros a la vista ticketScreen
//lector NFC crear codigo que busque una consulta al campo que tenga el valor de uidTag vacio "" actualizando con el valor del uidTag conseguido
//que el valor no pueda ser mayor al monto y que el monto sea uistate osea cada vez que aumenta el valor disminuya creando asi una concordancia con el pago
//contemplar entre payments y reloads