package com.softteco.template.ui.feature.signUp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.FieldDatePicker

import com.softteco.template.ui.components.SimpleField
import com.softteco.template.ui.components.TextFieldWithDropDownComponent
import com.softteco.template.ui.feature.login.LoginViewModel
import com.softteco.template.ui.theme.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
	modifier: Modifier = Modifier,
	viewModel: LoginViewModel = hiltViewModel(),
	onBackClicked: () -> Unit = {}
) {

	ScreenContent(
		modifier = modifier,
		//	viewModel = viewModel,
		onBackClicked = onBackClicked
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
	modifier: Modifier = Modifier,
	//viewModel: SignUpViewModel,
	onBackClicked: () -> Unit = {}
) {
	val scrollState = rememberScrollState()

	val context = LocalContext.current

	var firstName by remember { mutableStateOf("") }
	var lastName by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var confirmPassword by remember { mutableStateOf("") }
	var birthDay by remember { mutableStateOf("") }

	var country by remember { mutableStateOf("") }
	val countryList = mutableListOf<String>()
	val coroutineScope = rememberCoroutineScope()
	GetList.getList(coroutineScope, context, countryList)

	Box(modifier.fillMaxSize()) {
		Column {
			CustomTopAppBar(
				modifier = Modifier.fillMaxWidth(),
				stringResource(id = R.string.sign_up),
				showBackIcon = true,
				onBackClicked = onBackClicked
			)
			Column(
				modifier = Modifier
					.padding(Dimens.Padding20)
					.verticalScroll(scrollState),
				verticalArrangement = Arrangement.Center,
			) {
				Spacer(Modifier.size(Dimens.PaddingNormal))

				SimpleField(modifier = Modifier.fillMaxWidth(),
					strId = R.string.first_name,
					firstName,
					fieldErrorState = firstName.isEmpty(),
					onFieldValueChanged = { newValue -> firstName = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				SimpleField(modifier = Modifier.fillMaxWidth(),
					strId = R.string.last_name,
					lastName,
					fieldErrorState = lastName.isEmpty(),
					onFieldValueChanged = { newValue -> lastName = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				SimpleField(modifier = Modifier.fillMaxWidth(),
					strId = R.string.email,
					email,
					fieldErrorState = email.isEmpty(),
					onFieldValueChanged = { newValue -> email = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				SimpleField(modifier = Modifier.fillMaxWidth(),
					strId = R.string.password,
					password,
					fieldErrorState = password.isEmpty(),
					onFieldValueChanged = { newValue -> password = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				SimpleField(modifier = Modifier.fillMaxWidth(),
					strId = R.string.confirm_password,
					confirmPassword,
					fieldErrorState = confirmPassword.isEmpty(),
					onFieldValueChanged = { newValue -> confirmPassword = newValue })

				if (confirmPassword.isNotEmpty() && password.isNotEmpty()) {
					if (confirmPassword != password) {
						Text(
							text = stringResource(id = R.string.passwords_mismatching),
							color = Color.Red
						)
					}
				}

				Spacer(Modifier.size(Dimens.PaddingNormal))

				TextFieldWithDropDownComponent(modifier = Modifier.fillMaxWidth(),
					item = country,
					strId = R.string.country,
					fieldErrorState = country.isEmpty(),
					itemsList = countryList,
					onFieldValueChanged = { newValue -> country = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				FieldDatePicker(modifier = Modifier.fillMaxWidth(),
					birthDay,
					birthDay.isEmpty(),
					R.string.birth_day,
					onFieldValueChanged = { newValue -> birthDay = newValue })

				Spacer(Modifier.size(Dimens.PaddingNormal))

				Box(
					modifier = Modifier.padding(
						Dimens.Padding40, Dimens.Padding0, Dimens.Padding40, Dimens.Padding0
					)
				) {
					Button(
						onClick = {
							Toast.makeText(
								context, birthDay, Toast.LENGTH_SHORT
							).show()
						},
						shape = RoundedCornerShape(Dimens.Padding50),
						modifier = Modifier
							.fillMaxWidth()
							.height(Dimens.Padding50)
					) {
						Text(text = stringResource(id = R.string.sign_up))
					}
				}
			}
		}
	}
}

object GetList {
	fun getCountriesList(context: Context): List<String> {
		return context.assets.open("listCountries.txt").bufferedReader().use {
			it.readLines()
		}

	}

	suspend fun setList(context: Context, countryList: MutableList<String>) = coroutineScope {
		val message: Deferred<List<String>> = async { getCountriesList(context) }
		countryList.addAll(message.await())
	}

	fun getList(
		coroutineScope: CoroutineScope, context: Context, countryList: MutableList<String>
	) {
		coroutineScope.launch { setList(context, countryList) }
	}
}