package com.softteco.template.presentation.login.loginComponents

import androidx.compose.material3.DropdownMenu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DropDownListComponent(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit
) {
    DropdownMenu(expanded = requestToOpen, onDismissRequest = { request(false) }) {
        list.forEach {
            DropdownMenuItem(text = { Text(text = it) }, onClick = {
                request(false)
                selectedString(it)
            })
        }
    }
}