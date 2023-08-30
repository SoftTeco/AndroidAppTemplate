package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.Constants
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.login.CreateUserRepository
import com.softteco.template.data.login.model.CreateUserDto
import com.softteco.template.ui.feature.FieldValidationState
import com.softteco.template.ui.feature.ValidateFields
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val createUserRepository: CreateUserRepository,
) : ViewModel() {

	private val validateFields: ValidateFields = ValidateFields()

	private val loading = MutableStateFlow(false)
	val signUpState = MutableStateFlow(false)

	var passwordValue by mutableStateOf("")
	var emailValue by mutableStateOf("")

	fun register(
		user: CreateUserDto
	) = viewModelScope.launch {
		loading.value = true
		createUserRepository.registration(user).run {
			when (this) {
				is Result.Success -> signUpState.value = true
				is Result.Error -> signUpState.value = false
			}
		}
		loading.value = false
	}

	val state = combine(
		loading,
		signUpState
	) { loading, signUpState ->
		State(
			loading = loading,
			signUpState = signUpState
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	@Immutable
	data class State(
		val loading: Boolean = false,
		val signUpState: Boolean = false
	)

	@OptIn(ExperimentalCoroutinesApi::class)
	var passwordValidationError =
		snapshotFlow { passwordValue }
			.mapLatest { validateFields.validatePassword(it) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MILLIS),
				initialValue = FieldValidationState()
			)

	@OptIn(ExperimentalCoroutinesApi::class)
	var emailValidationError =
		snapshotFlow { emailValue }
			.mapLatest { validateFields.validateEmail(it) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MILLIS),
				initialValue = FieldValidationState()
			)

	fun changePassword(value: String) {
		passwordValue = value
	}

	fun changeEmail(value: String) {
		emailValue = value
	}
}