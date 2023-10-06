package com.softteco.template.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.ui.theme.Dimens

@Composable
fun PrimaryButton(
    buttonText: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isButtonEnabled = remember { mutableStateOf(true) }
    Box(
        modifier = modifier.padding(
            Dimens.PaddingLarge
        )
    ) {
        Button(
            enabled = isButtonEnabled.value,
            onClick = {
                onClick()
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.PaddingExtraLarge)

        ) {
            if (loading) {
                isButtonEnabled.value = false
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(Dimens.PaddingNormal)
                )
            } else {
                isButtonEnabled.value = true
                Text(text = buttonText)
            }
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(buttonText = "Button", loading = false, onClick = {})
}
