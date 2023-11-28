package com.softteco.template.ui.feature.settings

import android.annotation.SuppressLint
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomBottomSheet
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.ThemeMode

private const val ABOUT_URL = "https://softteco.com"
private const val TERMS_OF_SERVICES_URL = "https://softteco.com/terms-of-services"

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    ScreenContent(
        modifier = modifier,
        onBackClicked = onBackClicked,
        setThemeMode = settingsViewModel::setThemeMode,
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    setThemeMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val customIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(MaterialTheme.colorScheme.primary.toArgb())
                .build()
        )
        .build()

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTopAppBar(
                stringResource(id = R.string.settings),
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
            ListItem(
                modifier = Modifier.clickable { isSheetOpen = true },
                headlineContent = { Text(text = stringResource(id = R.string.theme)) },
                trailingContent = {
                    Icon(
                        Icons.Sharp.ArrowForwardIos,
                        contentDescription = "Settings description"
                    )
                },
            )
            Divider()
            ListItem(
                modifier = Modifier.clickable {
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(ABOUT_URL))
                },
                headlineContent = { Text(text = stringResource(id = R.string.about)) },
                trailingContent = {
                    Icon(
                        Icons.Sharp.ArrowForwardIos,
                        contentDescription = "Settings description"
                    )
                },
            )
            Divider()
            ListItem(
                modifier = Modifier.clickable {
                    customIntent.launchUrl(context, Uri.parse(TERMS_OF_SERVICES_URL))
                },
                headlineContent = { Text(text = stringResource(id = R.string.terms_of_services)) },
                trailingContent = {
                    Icon(
                        Icons.Sharp.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.terms_of_services)
                    )
                },
            )
            Divider()
            CustomBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                isSheetOpen = isSheetOpen,
                onDismissRequest = { isSheetOpen = false },
                onThemeSelected = { selectedTheme ->
                    setThemeMode(selectedTheme)
                    isSheetOpen = false
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(onBackClicked = {}, setThemeMode = {})
    }
}
