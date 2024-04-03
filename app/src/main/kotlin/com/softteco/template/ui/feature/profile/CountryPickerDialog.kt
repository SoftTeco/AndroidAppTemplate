package com.softteco.template.ui.feature.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingExtraSmall
import com.softteco.template.ui.theme.Dimens.PaddingSmall
import com.softteco.template.ui.theme.RoundedCornerSizes.Small

private const val COUNTY_LIST_HEIGHT_FRACTION = 0.3f

@Composable
internal fun CountryPickerDialog(
    country: String?,
    countries: List<String>,
    onCountryChanged: (String) -> Unit,
    onDismiss: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                country ?: "",
                TextRange(country?.length ?: 0)
            )
        )
    }

    AlertDialog(
        onDismissRequest = { onDismiss(null) },
        title = { Text(stringResource(R.string.change_country_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(PaddingDefault)) {
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) { focusRequester.requestFocus() }

                OutlinedTextField(
                    value = fieldValue,
                    onValueChange = { value ->
                        fieldValue = value
                        onCountryChanged(fieldValue.text)
                    },
                    Modifier.focusRequester(focusRequester),
                    label = { Text(stringResource(R.string.country_title)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                    ),
                    singleLine = true,
                )

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(Small))
                        .fillMaxHeight(COUNTY_LIST_HEIGHT_FRACTION),
                    verticalArrangement = Arrangement.spacedBy(PaddingExtraSmall),
                    contentPadding = PaddingValues(PaddingSmall),
                ) {
                    items(countries) { countryName ->
                        Text(
                            countryName,
                            Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { onDismiss(countryName) })
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(null) }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss(fieldValue.text) }) {
                Text(stringResource(R.string.ok))
            }
        },
        modifier = modifier,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CountryPickerDialog(
                country = "United States",
                countries = listOf(
                    "United States",
                    "Canada",
                    "Mexico",
                    "Colombia",
                    "Peru",
                    "Chile",
                    "Argentina",
                    "Brazil",
                    "Venezuela",
                    "Uruguay",
                    "Ecuador",
                    "Paraguay",
                    "Bolivia",
                ),
                onCountryChanged = {},
                onDismiss = {},
            )
        }
    }
}
