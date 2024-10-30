package com.puyodev.luka.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.puyodev.luka.R
import com.puyodev.luka.common.composable.*
import com.puyodev.luka.R.drawable as AppIcon
import com.puyodev.luka.R.string as AppText
import com.puyodev.luka.common.ext.card
import com.puyodev.luka.ui.theme.LukaTheme

@Composable
fun ProfileScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
){
    ProfileScreenContent(
        //uiState = uiState,
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) }
    )


}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    //uiState: SettingsUiState,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.neko),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
                    .clip(CircleShape),
            )
            Text(text = "PuyoDEV")
        }
        Column {
            SignOutCard { onSignOutClick() }
            DeleteMyAccountCard { onDeleteMyAccountClick() }

        }
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "name",
                onValueChange = { "name" },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "jeefry753@gmail.com",
                onValueChange = { "name" },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "902887796",
                onValueChange = { "name" },
                label = { Text("Número de celular") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "- - - - - - - -",
                onValueChange = { "name" },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botón para enviar el formulario
            Button(
                onClick = {
                    //isFormValid = name.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modificar")
            }
        }
    }
}
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        AppText.delete_my_account,
        AppIcon.ic_delete_my_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Preview
@Composable
fun PreviewProfile(){
    LukaTheme {
        ProfileScreenContent(
            onSignOutClick = { },
            onDeleteMyAccountClick = { },
        )
    }
}