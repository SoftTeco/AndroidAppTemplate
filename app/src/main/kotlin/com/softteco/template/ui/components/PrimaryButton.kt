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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.ui.theme.Dimens

@Composable
fun PrimaryButton(
    buttonText: String,
    showLoader: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.padding(
            Dimens.PaddingNormal,
            Dimens.PaddingLarge,
            Dimens.PaddingNormal
        )
    ) {
        Button(
            onClick = {
                onClick()
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.PaddingExtraLarge)

        ) {
            if (showLoader) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(Dimens.PaddingLarge)
                )
            } else {
                Text(text = buttonText)
            }
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(buttonText = "Button", showLoader = false, onClick = {})
}
