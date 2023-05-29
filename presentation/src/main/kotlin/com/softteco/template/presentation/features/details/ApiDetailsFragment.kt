package com.softteco.template.presentation.features.details

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.presentation.R
import com.softteco.template.presentation.common.AppTheme
import com.softteco.template.presentation.common.ComposeFragment
import com.softteco.template.presentation.common.PaddingDefault
import com.softteco.template.presentation.common.PaddingLarge
import com.softteco.template.presentation.common.PaddingMedium
import com.softteco.template.presentation.common.TextStyles
import com.softteco.template.presentation.common.popOrFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiDetailsFragment : ComposeFragment() {

    @Composable
    override fun Content() = ScreenContent(
        onBackPressed = ::popOrFinish,
        onLinkClicked = ::onLinkClicked
    )

    private fun onLinkClicked(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

@Composable
private fun ScreenContent(
    onBackPressed: () -> Unit,
    onLinkClicked: (String) -> Unit,
    viewModel: ApiDetailsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    Layout(state, onBackPressed, onToggleFavorite = viewModel::onToggleFavorite, onLinkClicked)
}

@Composable
private fun Layout(
    apiEntry: ApiEntry,
    onBackPressed: () -> Unit,
    onToggleFavorite: () -> Unit,
    onLinkClicked: (String) -> Unit,
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Toolbar(
            onBackPressed = onBackPressed,
            apiEntry = apiEntry,
            onToggleFavorite = onToggleFavorite
        )
        AsyncImage(
            modifier = Modifier
                .padding(PaddingLarge)
                .size(256.dp),
            model = apiEntry.logo,
            contentDescription = stringResource(R.string.description_api_logo),
            placeholder = painterResource(
                R.drawable.logo
            )
        )
        ApiDetails(apiEntry = apiEntry, onLinkClicked = { onLinkClicked(apiEntry.link) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Toolbar(
    onBackPressed: () -> Unit,
    apiEntry: ApiEntry,
    modifier: Modifier = Modifier,
    onToggleFavorite: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.shadow(elevation = 2.dp),
        title = {
            Text(text = apiEntry.name)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.background)
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back Btn")
            }
        },
        actions = {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    painterResource(
                        if (apiEntry.favorite) {
                            R.drawable.ic_favorite_filled_white
                        } else {
                            R.drawable.ic_favorite_outline_white
                        }
                    ),
                    null
                )
            }
        }
    )
}

@Composable
private fun ApiDetails(
    apiEntry: ApiEntry,
    modifier: Modifier = Modifier,
    onLinkClicked: () -> Unit,
) {
    Column(
        modifier
            .padding(PaddingMedium)
            .verticalScroll(rememberScrollState())
    ) {
        Detail(label = R.string.label_description, description = apiEntry.description)
        Detail(label = R.string.label_auth, description = apiEntry.auth)
        Detail(label = R.string.label_https, description = apiEntry.https.toString())
        Detail(label = R.string.label_cors, description = apiEntry.cors)
        Detail(label = R.string.label_category, description = apiEntry.category)
        Row(
            Modifier
                .padding(top = PaddingDefault)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = stringResource(R.string.label_link),
                style = TextStyles.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(PaddingMedium))
            Text(
                text = apiEntry.link,
                Modifier.clickable { onLinkClicked() },
                style = TextStyles.bodyMedium,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun Detail(
    @StringRes label: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .padding(top = PaddingDefault)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(label),
            textAlign = TextAlign.End,
            style = TextStyles.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(PaddingMedium))
        Text(text = description, style = TextStyles.bodyLarge)
    }
}

// region Previews
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_3A)
@Composable
private fun LayoutPreview() {
    AppTheme {
        val state = ApiEntry(
            name = "Axolotl",
            auth = "apiKey",
            category = "Animals",
            cors = "yes",
            description = "Collection of axolotl pictures and facts",
            https = true,
            link = "https://theaxolotlapi.netlify.app",
            logo = "https://api.dicebear.com/5.x/icons/svg?seed=Axolotl",
            favorite = false
        )
        Layout(state, {}, {}, {})
    }
}

// endregion
