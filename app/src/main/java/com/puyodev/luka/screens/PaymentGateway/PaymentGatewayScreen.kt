package com.puyodev.luka.screens.PaymentGateway

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puyodev.luka.screens.drawer.DrawerHeader
import com.puyodev.luka.screens.drawer.DrawerScreen
import com.puyodev.luka.common.composable.ActionToolbar
import com.puyodev.luka.common.ext.toolbarActions
import com.puyodev.luka.model.User
import com.puyodev.luka.screens.operation.OperationsViewModel
import kotlinx.coroutines.launch

@Composable
fun PaymentGatewayScreen(
    openScreen: (String) -> Unit,
    viewModel: PaymentGatewayViewModel = hiltViewModel()
) {
    // Observa un único objeto User en lugar de una lista
    val user by viewModel.user.collectAsStateWithLifecycle(initialValue = User())

    //val balance by viewModel.balance.collectAsStateWithLifecycle(initialValue = 0.0)

    PaymentGatewayScreenContent(
        user = user,
        //balance = balance,
        //onProfileClick = viewModel::onProfileClick,
        openScreen = openScreen,
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PaymentGatewayScreenContent(
    user: User,
    //balance: Double,
    //onProfileClick: ((String) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    openScreen: (String) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                DrawerHeader(user = user.username)
                Spacer(modifier = Modifier.height(16.dp))
                DrawerScreen(openScreen = openScreen)
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                ActionToolbar(
                    title = "Recargar Saldo",
                    modifier = Modifier.toolbarActions(),
                    endAction = { /*onProfileClick(openScreen) */},
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Saldo Actual: S/30.00",
                    style = MaterialTheme.typography.headlineMedium
                )

                // Campo para ingresar el monto de recarga
                var rechargeAmount by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = rechargeAmount,
                    onValueChange = { rechargeAmount = it },
                    label = { Text("Monto de Recarga") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón para realizar la recarga
                Button(
                    onClick = {
                        // Lógica para recargar (puedes vincularlo a viewModel)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Recargar")
                }
            }
        }
    }
}