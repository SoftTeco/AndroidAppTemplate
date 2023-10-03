package com.softteco.template.ui.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.RoundedCornerSizes

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit,
    onSignatureClicked: () -> Unit,
    onBleClicked: () -> Unit,
) {
    val cards = listOf(
        HomeCards.LoginCard(onLoginClicked),
        HomeCards.SignatureCard(onSignatureClicked),
        HomeCards.BleCard(onBleClicked),
    )
    ScreenContent(modifier = modifier, cards = cards)
}

@Composable
private fun ScreenContent(
    cards: List<HomeCards>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(Dimens.PaddingSmall)
        ) {
            items(cards) { item ->
                HomeCard(
                    title = stringResource(id = item.titleId),
                    description = stringResource(id = item.descriptionId),
                    drawableRes = item.drawableRes,
                    onClick = item.onClick,
                    isEnabled = item.enabled,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeCard(
    title: String,
    description: String,
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    Card(
        enabled = isEnabled,
        modifier = modifier
            .padding(Dimens.PaddingSmall),
        onClick = { if (isEnabled) onClick() },
        shape = RoundedCornerShape(RoundedCornerSizes.Medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingDefault),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(drawableRes),
                contentDescription = title,
            )
        }
    }
}

sealed class HomeCards(
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int,
    @DrawableRes val drawableRes: Int,
    val onClick: () -> Unit = {},
    val enabled: Boolean = true,
) {

    class LoginCard(onClick: () -> Unit) : HomeCards(
        titleId = R.string.login,
        descriptionId = R.string.login_description,
        drawableRes = R.drawable.baseline_login_24,
        onClick = onClick,
    )

    class SignatureCard(onClick: () -> Unit) : HomeCards(
        titleId = R.string.signature,
        descriptionId = R.string.signature_description,
        drawableRes = R.drawable.baseline_design_services_24,
        onClick = onClick,
        enabled = false,
    )

    class BleCard(onClick: () -> Unit) : HomeCards(
        titleId = R.string.ble,
        descriptionId = R.string.ble_description,
        drawableRes = R.drawable.baseline_bluetooth_24,
        onClick = onClick,
        enabled = false,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        val cards = listOf(
            HomeCards.LoginCard(onClick = {}),
            HomeCards.SignatureCard(onClick = {}),
            HomeCards.BleCard(onClick = {}),
        )
        ScreenContent(cards = cards)
    }
}
