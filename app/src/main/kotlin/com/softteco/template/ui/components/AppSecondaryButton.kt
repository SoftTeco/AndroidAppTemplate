package com.softteco.template.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun SecondaryButton(
    title: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
        onClick = { if (!loading) onClick() },
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(Dimens.PaddingNormal))
        } else {
            Text(text = title)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        SecondaryButton(title = "Button", loading = true, onClick = {})
    }
}
