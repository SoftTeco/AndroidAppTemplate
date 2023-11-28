package com.softteco.template.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppListItem(
    onClick: () -> Unit,
    title: String,
    iconDescription: String,
    imageIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        headlineContent = { Text(text = title) },
        trailingContent = {
            Icon(
                imageIcon,
                contentDescription = iconDescription
            )
        },
    )
}
