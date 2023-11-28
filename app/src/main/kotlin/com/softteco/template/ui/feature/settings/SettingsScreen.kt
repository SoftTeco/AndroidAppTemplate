package com.softteco.template.ui.feature.settings

import android.annotation.SuppressLint
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.Constants
import com.softteco.template.R
import com.softteco.template.ui.components.AppListItem
import com.softteco.template.ui.components.CustomBottomSheet
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.sendMail

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
            AppListItem(
                onClick = { isSheetOpen = true },
                title = stringResource(id = R.string.theme),
                imageIcon = Icons.Sharp.ArrowForwardIos,
                iconDescription = stringResource(id = R.string.theme),
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            AppListItem(
                onClick = {
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(ABOUT_URL))
                },
                title = stringResource(id = R.string.about),
                imageIcon = Icons.Sharp.ArrowForwardIos,
                iconDescription = stringResource(id = R.string.about),
                modifier = Modifier.fillMaxWidth()
            )

            Divider()
            AppListItem(
                onClick = {
                    context.sendMail(
                        recipient = Constants.CONTACT_EMAIL,
                        subject = Constants.CONTACT_SUBJECT
                    )
                },
                title = stringResource(id = R.string.contact_us),
                imageIcon = Icons.Sharp.ArrowForwardIos,
                iconDescription = stringResource(id = R.string.contact_us),
                modifier = Modifier.fillMaxWidth()
            )

            Divider()
            AppListItem(
                onClick = {
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(TERMS_OF_SERVICES_URL))
                },
                title = stringResource(id = R.string.terms_of_services),
                imageIcon = Icons.Sharp.ArrowForwardIos,
                iconDescription = stringResource(id = R.string.terms_of_services),
                modifier = Modifier.fillMaxWidth()
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
