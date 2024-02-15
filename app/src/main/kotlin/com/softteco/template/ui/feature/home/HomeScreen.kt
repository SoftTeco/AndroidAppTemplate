package com.softteco.template.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.utils.Analytics

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Analytics.homeOpened()
    ScreenContent(modifier = modifier)
}

@Composable
private fun ScreenContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.app_name))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent()
    }
}
