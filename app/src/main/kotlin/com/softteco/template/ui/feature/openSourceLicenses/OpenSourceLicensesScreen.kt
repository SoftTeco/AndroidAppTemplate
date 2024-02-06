package com.softteco.template.ui.feature.openSourceLicenses

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.utils.Analytics
import com.softteco.template.utils.getHyperLinks

private const val TAG = "URL"

@Composable
fun OpenSourceLicensesScreen(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OpenSourceLicensesViewModel = hiltViewModel(),
) {
    val licenses by viewModel.licensesList.collectAsState()
    Analytics.licensesOpened()

    ScreenContent(
        onBackClicked = onBackClicked,
        licenses = licenses,
        modifier = modifier
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    licenses: List<LibraryItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.open_source_licenses),
            showBackIcon = true,
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked
        )
        if (licenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(id = R.string.no_open_source_licenses),
                )
            }
        }
        LazyColumn {
            items(licenses) { license ->
                ItemView(license, modifier)
            }
        }
    }
}

@Composable
fun ItemView(
    license: LibraryItem,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    val listOfUrls = getHyperLinks(license.license)

    Column(
        modifier = Modifier
            .clickable {
                showDialog = true
            }
    ) {
        Text(
            license.name,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(vertical = Dimens.PaddingSmall, horizontal = Dimens.PaddingDefault)
                .fillMaxWidth()
        )
        Divider()
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                ElevatedCard(
                    shape = ShapeDefaults.Small,
                    modifier = Modifier
                        .padding(vertical = Dimens.PaddingExtraLarge)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(
                                horizontal = Dimens.PaddingNormal,
                                vertical = Dimens.PaddingNormal
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = license.name,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
                        )
                        AnnotateText(
                            highlightList = listOfUrls,
                            originalText = license.license,
                            modifier = modifier,
                        )
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(top = Dimens.PaddingSmall)
                                .clickable { showDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnnotateText(
    highlightList: List<Pair<Int, Int>>,
    originalText: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var annotatedString = buildAnnotatedString { append(originalText) }

    for (item in highlightList) {
        annotatedString = buildAnnotatedString {
            val highlightText = originalText.substring(item.first, item.second)
            addStringAnnotation(
                tag = TAG,
                annotation = highlightText,
                start = item.first,
                end = item.second
            )
            append(annotatedString)
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                ),
                start = item.first,
                end = item.second
            )
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        onClick = {
            annotatedString
                .getStringAnnotations(TAG, it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    val intent = CustomTabsIntent
                        .Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(stringAnnotation.item))
                }
        }
    )
}

data class LibraryItem(val name: String, val license: String)

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            onBackClicked = {},
            licenses = listOf(LibraryItem("Activity", "http://www.apache.org/licenses/"))
        )
    }
}
