package com.puyodev.luka.screens.splash

import androidx.compose.foundation.Image//para Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.puyodev.luka.R
import com.puyodev.luka.R.string as AppText
import com.puyodev.luka.ui.theme.LukaTheme
import kotlinx.coroutines.delay

import com.puyodev.luka.common.composable.BasicButton
import com.puyodev.luka.common.ext.basicButton
import android.util.Log // Asegúrate de importar Log

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Log.d("SplashScreen", "SplashScreen Composable inicializado")
    SplashScreenContent(
        onAppStart = {
            Log.d("SplashScreen", "Llamando a onAppStart desde SplashScreenContent")
            viewModel.onAppStart(openAndPopUp)
        },
        shouldShowError = viewModel.showError.value
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onAppStart: () -> Unit,
    shouldShowError: Boolean
){
    Log.d("SplashScreenContent", "Contenido de SplashScreen cargado")
    if (shouldShowError) {
        Log.d("SplashScreenContent", "Mostrando error en SplashScreenContent")
        Text(text = stringResource(AppText.generic_error))

        BasicButton(AppText.try_again, Modifier.basicButton()) {
            Log.d("SplashScreenContent", "Intentando de nuevo desde el botón de error")
            onAppStart()
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ciudad02),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(id = R.drawable.logo_luka),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(200.dp)
            )
        }
    }

    LaunchedEffect(true) {
        Log.d("SplashScreenContent", "Iniciando delay en LaunchedEffect")
        delay(SPLASH_TIMEOUT)
        Log.d("SplashScreenContent", "Finalizado delay en LaunchedEffect, llamando a onAppStart")
        onAppStart()
    }
}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    LukaTheme {
        SplashScreenContent(
            onAppStart = { },
            shouldShowError = false
        )
    }
}
