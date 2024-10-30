package com.puyodev.luka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.puyodev.luka.navigation.AppScreens
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent() {
    val items = listOf(
        NavigationItems(
            title = "Inicio",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItems(
            title = "Info",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItems(
            title = "Editar",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            badgeCount = 105
        ),
        NavigationItems(
            title = "Configuraciones",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        NavigationItems(
            title = "Historial",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email
        )
    )

    //Remember Clicked index state
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    items.forEachIndexed { index, item ->
        Spacer(modifier = Modifier.height(16.dp)) //space (margin) from top
        NavigationDrawerItem(
            label = { Text(text = item.title) },
            selected = index == selectedItemIndex,
            onClick = {
                selectedItemIndex = index
/*
                // Lógica de navegación basada en el título del ítem seleccionado
                when (item.title) {
                    "Home" -> navController.navigate("home")
                    "Info" -> navController.navigate("info")
                    "Edit" -> navController.navigate("edit")
                    "Settings" -> navController.navigate("settings")
                    "Historial" -> navController.navigate(AppScreens.HistoryScreen.route)
                }
*/
                scope.launch {
                    drawerState.close()
                }
            },
            icon = {
                Icon(
                    imageVector = if (index == selectedItemIndex) {
                        item.selectedIcon
                    } else item.unselectedIcon,
                    contentDescription = item.title
                )
            },
            shape = MaterialTheme.shapes.small,
            badge = {  // Show Badge
                item.badgeCount?.let {
                    Text(text = item.badgeCount.toString())
                }
            },
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
        )
    }
}