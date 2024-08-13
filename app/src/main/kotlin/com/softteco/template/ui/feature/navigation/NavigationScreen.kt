package com.softteco.template.ui.feature.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.navigation.dropin.NavigationView
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.RequestLocationPermissionDialog
import com.softteco.template.ui.feature.navigation.androidauto.CarAppSyncComponent
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.utils.Analytics

@Composable
fun NavigationScreen(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        Analytics.navigationOpened()
    }
    RequestLocationPermissionDialog()
    ScreenContent(
        modifier = modifier,
        onBackClicked = onBackClicked,
    )
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.navigation),
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked
        )
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx: Context ->
                NavigationView(
                    context = ctx,
                    accessToken = ctx.getString(R.string.mapbox_access_token)
                )
                    .apply {
                        this.api.routeReplayEnabled(true)
                        CarAppSyncComponent.getInstance().setNavigationView(this)
                    }
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent()
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    AppTheme {
        ScreenContent()
    }
}
