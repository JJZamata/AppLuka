
package com.puyodev.luka.screens.ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puyodev.luka.R
import com.puyodev.luka.model.User
//import com.example.makeitso.model.Task
import com.puyodev.luka.ui.theme.LukaTheme

@Composable
fun TicketScreen(
    valor: Int?,
    direccion: String?,
    openScreen: (String) -> Unit,
    viewModel: TicketViewModel = hiltViewModel(),
) {
    // Observa un único objeto User en lugar de una lista
    val user by viewModel.user.collectAsStateWithLifecycle(initialValue = User())


    TicketScreenContent(
        user = user,
        valor = valor,  // Pasamos el valor como parámetro
        direccion = direccion,  // Pasamos la dirección como parámetro
        onPayScreenClick = viewModel::onPayScreenClick,
        openScreen = openScreen
    )
}

@Composable
fun TicketScreenContent(
    modifier: Modifier = Modifier,
    user: User, // Añade este parámetro
    valor: Int?,  // Recibe el parámetro valor
    direccion: String?,  // Recibe el parámetro direccion
    onPayScreenClick: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit

) {
    val fecha = "31/10/2024"
    Column(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    // use Brush.verticalGradeint to change the angle
                    colors = listOf(
                        Color(0xFF64B5F6), // Start color
                        Color(0xFF0D47A1)  // End color
                    )
                )
            )
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 80.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.Center // Centra los elementos dentro de Row
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_luka),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp), // Tamaño personalizado para la imagen centrada
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centra el Box en la pantalla
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(25.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Centra el Box en la pantalla
                ) {
                    Text(text = fecha)
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp, 40.dp)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bus),
                            contentDescription = null,
                            modifier = Modifier
                                .size(160.dp) // Tamaño personalizado para la imagen centrada
                        )
                        Text(text = "Bus 101")
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center // Centra el contenido del Row horizontalmente

                        ) {
                            Text(
                                text = "Pago:\nS/$valor",
                                textAlign = TextAlign.Center
                            )
                        }
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center // Centra el contenido del Row horizontalmente

                        ) {
                            Text(
                                text = "Paradero:\n$direccion",
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los elementos de forma uniforme
        ) {
            SmallFloatingActionButton(
                onClick = { onPayScreenClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    Icons.Outlined.Share,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(10.dp),
                    contentDescription = "" // Add a valid content description
                )
            }
            SmallFloatingActionButton(
                onClick = { onPayScreenClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    Icons.Filled.Menu,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(10.dp),
                    contentDescription = "" // Add a valid content description
                )
            }
            SmallFloatingActionButton(
                onClick = { onPayScreenClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(10.dp),
                    contentDescription = "" // Add a valid content description
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketScreenPreview() {
    LukaTheme {
        TicketScreen(
            valor = 5,  // Ejemplo de valor para "Pago"
            direccion = "Av. Principal",  // Ejemplo de dirección o paradero
            openScreen = {}  // Acción de navegación vacía para el Preview
        )
    }
}