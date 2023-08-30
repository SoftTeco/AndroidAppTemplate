package com.softteco.template.ui.feature.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.login.LoginRepository
import com.softteco.template.data.login.model.LoginAuthDto
import com.softteco.template.ui.components.SnackBarState
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
class LoginViewModel @Inject constructor(
	private val loginRepository: LoginRepository,
) : ViewModel() {

	private val validateFields: ValidateFields = ValidateFields()

	private val loading = MutableStateFlow(false)
	val loginState = MutableStateFlow(false)

	var value by mutableStateOf("")

	fun login(
		userAuth: LoginAuthDto
	) = viewModelScope.launch {
		loading.value = true
		loginRepository.login(userAuth).run {
			when (this) {
				is Result.Success -> loginState.value = true
				is Result.Error -> loginState.value = false
			}
		}
		loading.value = false
	}

	val state = combine(
		loading,
		loginState
	) { loading, loginState ->
		State(
			loading = loading,
			loginState = loginState
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	@Immutable
	data class State(
		val loading: Boolean = false,
		val loginState: Boolean = false
	)

	@OptIn(ExperimentalCoroutinesApi::class)
	var fieldValidationError =
		snapshotFlow { value }
			.mapLatest { validateFields.execute(it) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000L),
				initialValue = FieldValidationState()
			)
}