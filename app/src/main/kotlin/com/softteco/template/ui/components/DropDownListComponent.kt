package com.softteco.template.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropDownListComponent(
    list: List<String>,
    request: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    requestToOpen: Boolean = false,
    selectedString: (String) -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = requestToOpen,
        onDismissRequest = { request(false) }
    ) {
        list.forEach {
            DropdownMenuItem(text = { Text(text = it) }, onClick = {
                request(false)
                selectedString(it)
            })
        }
    }
}
