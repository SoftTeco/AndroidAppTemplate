package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.components.SimpleFieldState
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.ValidateFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val repository: ProfileRepository,
) : ViewModel() {

	private var fieldValidationState = MutableStateFlow(ValidateFields())

	private val loading = MutableStateFlow(false)
	private var snackBarState = MutableStateFlow(SnackBarState())

	private val signUpState = MutableStateFlow(false)

	private var fieldState = MutableStateFlow(SimpleFieldState())

	private var firstNameStateValue = MutableStateFlow("")
	private var lastNameStateValue = MutableStateFlow("")

	fun register(
		user: CreateUserDto
	) = viewModelScope.launch {
		loading.value = true
		repository.registration(user).run {
			when (this) {
				is Result.Success -> signUpState.value = true
				is Result.Error -> handleError()
			}
		}
		loading.value = false
	}


	val state = combine(
		loading,
		snackBarState,
		signUpState,
		firstNameStateValue,
		lastNameStateValue

	) { loading, snackBar, signUpState, firstName, lastName ->
		State(
			loading = loading,
			snackBar = snackBar,
			signUpState = signUpState,
			firstNameValue = firstName,
			isFirstNameFieldEmpty = fieldValidationState.value.validateFieldEmpty(firstName).isEmpty,
			firstNameChanged = { firstNameStateValue.value = it },
			lastNameValue = lastName,
			isLastNameFieldEmpty = fieldValidationState.value.validateFieldEmpty(lastName).isEmpty,
			lastNameChanged = { lastNameStateValue.value = it },
			dismissSnackBar = { snackBarState.value = SnackBarState() }
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	val simpleFieldState = combine(fieldState, firstNameStateValue) { fieldState ->
		SimpleStateField(
			fieldState = SimpleFieldState(
				R.string.required, Color.Red,
				fieldValidationState.value.validateFieldEmpty(firstNameStateValue.value).isEmpty
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		SimpleStateField()
	)

	private fun handleError() {
		signUpState.value = false
		snackBarState.value = SnackBarState(
//            if (isAllFieldsValid.value) {
//                R.string.problem_error
//            } else {
//                R.string.empty_fields_error
//            },
			R.string.empty_fields_error,
			true
		)
	}

	@Immutable
	data class State(
		val loading: Boolean = false,
		val snackBar: SnackBarState = SnackBarState(),
		val signUpState: Boolean = false,
		val firstNameValue: String = "",
		val isFirstNameFieldEmpty: Boolean = false,
		val firstNameChanged: (String) -> Unit = {},
		val lastNameValue: String = "",
		val isLastNameFieldEmpty: Boolean = false,
		val lastNameChanged: (String) -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)

	@Immutable
	data class SimpleStateField(
		val fieldState: SimpleFieldState = SimpleFieldState(),
		val value: String = "",
	)
}
