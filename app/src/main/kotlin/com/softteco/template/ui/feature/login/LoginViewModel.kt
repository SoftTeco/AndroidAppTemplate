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

	private val validatePassword: ValidateFields = ValidateFields()

	private val snackbarState = MutableStateFlow(SnackBarState())
	private val loading = MutableStateFlow(true)
	val loginState = MutableStateFlow(false)

	var value by mutableStateOf("")
		private set

	fun login(
		userAuth: LoginAuthDto
	) = viewModelScope.launch {
		loginRepository.login(userAuth).run {
			when (val result = this) {
				is Result.Success -> loginState.value = true
				is Result.Error -> handleError(result.error)
			}
		}
		loading.value = false
	}

	private fun handleError(error: ErrorEntity) {
		if (error.isDisplayable) {
			val textId = when (error) {
				ErrorEntity.AccessDenied -> R.string.error_example
				ErrorEntity.Network -> R.string.error_example
				ErrorEntity.NotFound -> R.string.error_example
				ErrorEntity.ServiceUnavailable -> R.string.error_example
				ErrorEntity.Unknown -> R.string.error_example
			}
			snackbarState.value = SnackBarState(
				textId = textId,
				show = true,
			)
		}
	}


	val state = combine(
		loading,
		loginState,
		snackbarState
	) { loading, loginState, snackbar ->
		State(
			loading = loading,
			loginState = loginState,
			snackbar = snackbar,
			dismissSnackBar = { snackbarState.value = SnackBarState() }
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	@Immutable
	data class State(
		val loading: Boolean = false,
		val loginState: Boolean = false,
		val snackbar: SnackBarState = SnackBarState(),
		val dismissSnackBar: () -> Unit = {},
	)

	@OptIn(ExperimentalCoroutinesApi::class)
	val fieldValidationError =
		snapshotFlow { value }
			.mapLatest { validatePassword.execute(it) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000L),
				initialValue = FieldValidationState()
			)
	fun changeFieldValue(value: String) {
		this.value = value
	}
}