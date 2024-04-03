package com.softteco.template.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
    isSheetOpen: Boolean = false,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onDismissRequest() },
            modifier = modifier,
        ) {
            ThemeListItem(ThemeMode.SystemDefault, onThemeSelected)
            Divider(Modifier.padding(horizontal = PaddingDefault))
            ThemeListItem(ThemeMode.Dark, onThemeSelected)
            Divider(Modifier.padding(horizontal = PaddingDefault))
            ThemeListItem(ThemeMode.Light, onThemeSelected)
        }
    }
}

@Composable
private fun ThemeListItem(
    themeMode: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onThemeSelected(themeMode) },
        headlineContent = { Text(text = themeMode.value) }
    )
}
