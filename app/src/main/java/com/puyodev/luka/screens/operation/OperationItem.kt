package com.puyodev.luka.screens.operation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puyodev.luka.R.drawable as AppIcon
import com.puyodev.luka.common.composable.DropdownContextMenu
import com.puyodev.luka.common.ext.contextMenu
import com.puyodev.luka.common.ext.hasCreatedDate
import com.puyodev.luka.common.ext.hasCreatedTime
import com.puyodev.luka.common.ext.hasTitle
import com.puyodev.luka.common.ext.hasType
import com.puyodev.luka.model.Operation
import com.puyodev.luka.screens.Operacion
import com.puyodev.luka.ui.theme.LukaTheme
import java.lang.StringBuilder

@Composable
fun OperationItem(
    operation: Operation,
    //options: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(getCreatedTypeAndTitle(operation), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = getCreatedDateAndTime(operation),
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.6f) ,// Opacidad equivalente a ContentAlpha.medium
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = operation.mount,
                style = MaterialTheme.typography.bodyMedium,
                color = if (operation.type=="Recarga") {
                    Color(0xFF4CAF50) // Verde para recargas
                } else {
                    Color(0xFFF44336) // Rojo para pagos
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
/*
            if (task.flag) {
                Icon(
                    painter = painterResource(AppIcon.ic_flag),
                    tint = DarkOrange,
                    contentDescription = "Flag"
                )
            }
*/
            //DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
    }
}

private fun getCreatedDateAndTime(operation: Operation): String {
    val stringBuilder = StringBuilder("")

    if (operation.hasCreatedDate()) {
        stringBuilder.append(operation.dueDate)
        stringBuilder.append(" ")
    }

    if (operation.hasCreatedTime()) {
        stringBuilder.append("a las ")
        stringBuilder.append(operation.dueTime)
    }

    return stringBuilder.toString()
}

private fun getCreatedTypeAndTitle(operation: Operation): String {
    val stringBuilder = StringBuilder("")

    if (operation.hasType()) {
        stringBuilder.append(operation.type)
        stringBuilder.append(" ")
    }

    if (operation.hasTitle()) {
        stringBuilder.append(": ")
        stringBuilder.append(operation.title)
    }

    return stringBuilder.toString()
}