package com.softteco.template.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.softteco.template.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
	modifier: Modifier = Modifier,
	title: String,
	showBackIcon: Boolean,
	onBackClicked: () -> Unit = {},
) {

	TopAppBar(
		modifier = modifier,
		title = {
			Text(text = title)
		},
		colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
		navigationIcon = {
			if (showBackIcon) {
				run {
					IconButton(onClick = { onBackClicked() }) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = stringResource(R.string.back_description)
						)
					}
				}
			}
		},
	)
}
