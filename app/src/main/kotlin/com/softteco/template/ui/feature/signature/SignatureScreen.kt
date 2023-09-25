package com.softteco.template.ui.feature.signature

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

@Composable
fun SignatureScreen(
    onBackClicked: () -> Unit,
    viewModel: SignatureViewModel = hiltViewModel(),
) {
    val signatureFilePath by viewModel.signatureFilePath.collectAsState()
    ScreenContent(
        signatureFilePath = signatureFilePath,
        onBackClicked = onBackClicked,
        saveAsJpg = { viewModel.saveSignatureAsJPG(it) },
        saveSignatureAsSvg = { viewModel.saveSignatureAsSvg(it) },
        onDeleteSignatureClicked = { viewModel.deleteSignature(it) }
    )
}

@Composable
private fun ScreenContent(
    signatureFilePath: String,
    onBackClicked: () -> Unit,
    saveAsJpg: (Bitmap) -> Unit,
    saveSignatureAsSvg: (String) -> Unit,
    onDeleteSignatureClicked: (String) -> Unit
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomTopAppBar(
            onBackClicked = onBackClicked,
            showBackIcon = true,
            title = stringResource(id = R.string.signature),
        )
        SignatureDrawPanel(
            modifier = Modifier.weight(1f),
            saveAsJpg = saveAsJpg,
            saveSignatureAsSvg = saveSignatureAsSvg,
        )
        SignatureViewPanel(
            modifier = Modifier.weight(1f),
            signatureFilePath = signatureFilePath,
            onDeleteSignatureClicked = onDeleteSignatureClicked,
        )
    }
}

@Composable
private fun SignatureDrawPanel(
    saveAsJpg: (Bitmap) -> Unit,
    saveSignatureAsSvg: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var signaturePadAdapter: SignaturePadAdapter? = null
    Column(
        modifier = modifier
            .padding(PaddingDefault)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.sign_here),
            modifier = Modifier.padding(top = PaddingDefault)
        )
        SignaturePadView(
            modifier = Modifier
                .weight(1f)
                .padding(PaddingDefault)
                .background(MaterialTheme.colorScheme.background),
            onReady = {
                signaturePadAdapter = it
            }
        )
        SignaturePadControls(
            saveAsJpg = { signaturePadAdapter?.getSignatureBitmap()?.let { saveAsJpg(it) } },
            saveSignatureAsSvg = {
                saveSignatureAsSvg(
                    signaturePadAdapter?.getSignatureSvg() ?: ""
                )
            },
            clear = { signaturePadAdapter?.clear() },
            modifier = Modifier.padding(top = PaddingDefault)
        )
    }
}

@Composable
private fun SignaturePadControls(
    saveAsJpg: () -> Unit,
    saveSignatureAsSvg: () -> Unit,
    clear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = clear) {
            Text(stringResource(id = R.string.clear))
        }
        Button(onClick = saveSignatureAsSvg) {
            Text(stringResource(id = R.string.save_as_svg))
        }
        Button(onClick = saveAsJpg) {
            Text(stringResource(id = R.string.save_as_jpg))
        }
    }
}

@Composable
private fun SignatureViewPanel(
    signatureFilePath: String,
    onDeleteSignatureClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingDefault)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (signatureFilePath.isNotEmpty()) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(signatureFilePath)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(PaddingDefault),
                    contentScale = ContentScale.Inside,
                    contentDescription = stringResource(id = R.string.signature)
                )
                Button(onClick = { onDeleteSignatureClicked(signatureFilePath) }) {
                    Text(stringResource(id = R.string.delete_file))
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.empty_here),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
