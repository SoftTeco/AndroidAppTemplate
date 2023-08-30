package com.softteco.template.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softteco.template.R

@Composable
fun TextFieldWithDropDownComponent(
	modifier: Modifier = Modifier,
	item: String,
	strId: Int,
	fieldErrorState: Boolean,
	itemsList: List<String>,
	onFieldValueChanged: ((String) -> Unit) = {}
) {
	val userSelectedString: (String) -> Unit = {
		onFieldValueChanged(it)
	}
	val isOpen = remember { mutableStateOf(false) }
	val openCloseOfDropDownList: (Boolean) -> Unit = {
		isOpen.value = it
	}
	Box(modifier = modifier) {
		Column {
			OutlinedTextField(
				value = item,
				onValueChange = {
					onFieldValueChanged(it)
				},
				modifier = Modifier.fillMaxWidth(),
				isError = fieldErrorState,
				label = {
					Text(text = stringResource(id = strId))
				},
			)
			DropDownListComponent(
				list = itemsList,
				openCloseOfDropDownList,
				modifier = Modifier.fillMaxSize(),
				requestToOpen = isOpen.value,
				userSelectedString
			)
		}
		Spacer(
			modifier = Modifier
				.matchParentSize()
				.background(Color.Transparent)
				.padding(10.dp)
				.clickable(onClick = { isOpen.value = true })
		)
	}
	if (fieldErrorState) {
		Text(text = stringResource(id = R.string.required), color = Color.Red)
	}
}