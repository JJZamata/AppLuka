package com.puyodev.luka.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.puyodev.luka.R
import com.puyodev.luka.navigation.AppScreens
import kotlinx.coroutines.launch
/*
@Composable
fun AppContent(navController: NavController) {
    var valor by remember { mutableIntStateOf(1) } // Estado del contador
    val drawerState = rememberDrawerState(DrawerValue.Closed) // Estado para abrir/cerrar el drawer
    val scope = rememberCoroutineScope() // Alcance de la corrutina para manejar el drawer

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState, // Controla si el drawer está abierto o cerrado
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    DrawerHeader()
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio desde el borde superior
                    DrawerContent(navController)
                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                topBar = {
                    CustomTopBar(navController = navController, name = "PuyoDEV", onMenuClick = {
                        scope.launch { drawerState.open() } // Abre el drawer al hacer clic en el menú
                    })
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
                            onClick = { /*TODO*/ }
                        ) {
                            Text("Monto\nS/35.00")
                        }
                        OutlinedButton(
                            modifier = Modifier.width(160.dp),
                            onClick = { /*TODO*/ }
                        ) {
                            Text("Tarifa\nS/1.00")
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
                                if (valor < 9) valor++ // Suma 1 si valor es menor a 10 - límite 10
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
                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(0.dp, 40.dp),
                            onClick = {
                                navController.navigate(AppScreens.PaymentScreen.route + "/102/Av. Viña del Mar 705/$valor/02-05-2024 - 21:04")
                            },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            icon = {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Realizar pago" // Add a valid content description
                                )
                            },
                            text = { Text(text = "Pagar", fontSize = 20.sp) }
                        )
                    }
                }
            }
        }
    }
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
            verticalAlignment = Alignment.CenterVertically, // Centra verticalmente el contenido del Row
            horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los elementos de forma uniforme
        ) {
            Text(text = "Ubicación Actual: Av.\n Mariscal Castilla #203", fontSize = 20.sp)
            Image(
                painter = painterResource(id = R.drawable.located),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp) // Tamaño personalizado para la imagen centrada
            )

        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.neko),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            //style = MaterialTheme.typography.bodyLarge,
            //color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}*/