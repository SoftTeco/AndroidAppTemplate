package com.softteco.template.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.ui.theme.Dimens

@Composable
fun PrimaryButton(
    buttonText: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { if (!loading) onClick() },
        shape = MaterialTheme.shapes.large,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(Dimens.PaddingNormal)
            )
        } else {
            Text(text = buttonText)
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(buttonText = "Button", loading = false, onClick = {})
}
