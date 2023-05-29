package com.softteco.template.presentation.features.favorites

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.presentation.R
import com.softteco.template.presentation.common.AppTheme
import com.softteco.template.presentation.common.ComposeFragment
import com.softteco.template.presentation.common.ContentCardCornerSize
import com.softteco.template.presentation.common.navigateSafe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : ComposeFragment() {

    @Composable
    override fun Content() = ScreenContent(
        onItemClicked = { entry ->
            findNavController().navigateSafe(FavoritesFragmentDirections.favoritesToDetails(entry))
        }
    )
}

@Composable
private fun ScreenContent(
    viewModel: FavoritesViewModel = viewModel(),
    onItemClicked: (ApiEntry) -> Unit
) {
    val state by viewModel.state.collectAsState()
    Layout(state, onItemClicked = onItemClicked)
}

@Composable
private fun Layout(
    apiEntries: List<ApiEntry>,
    onItemClicked: (ApiEntry) -> Unit,
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Toolbar()
        FavoritesList(apiEntries = apiEntries, onItemClicked = onItemClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Toolbar(
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.shadow(elevation = 2.dp),
        title = {
            Text(text = stringResource(id = R.string.favorites))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.background)
        ),
    )
}

@Composable
private fun FavoritesList(
    apiEntries: List<ApiEntry>,
    modifier: Modifier = Modifier,
    onItemClicked: (ApiEntry) -> Unit,
) {
    LazyColumn {
        items(apiEntries) { item: ApiEntry ->
            FavoriteItem(modifier = modifier, apiEntry = item, onItemClicked = onItemClicked)
        }
    }
}

@Composable
private fun FavoriteItem(
    apiEntry: ApiEntry,
    modifier: Modifier = Modifier,
    onItemClicked: (ApiEntry) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(apiEntry)
            },
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(ContentCardCornerSize)
                .height(56.dp)
                .align(Alignment.CenterVertically),
            model = apiEntry.logo,
            contentDescription = stringResource(R.string.description_api_logo),
            placeholder = painterResource(R.drawable.logo)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = apiEntry.name
        )
    }
}

// region Previews
val apiEntry = ApiEntry(
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

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_3A)
@Composable
private fun LayoutPreview() {
    AppTheme {
        val state = listOf(apiEntry, apiEntry, apiEntry)
        Layout(state) {}
    }
}

@Preview
@Composable
private fun FavoriteItemPreview() {
    FavoriteItem(modifier = Modifier, apiEntry = apiEntry) {}
}

// endregion
