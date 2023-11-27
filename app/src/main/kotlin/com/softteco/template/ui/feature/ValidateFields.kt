package com.softteco.template.ui.feature

import com.softteco.template.Constants
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.ui.feature.ValidateFields.isHasCapitalizedLetter
import com.softteco.template.ui.feature.ValidateFields.isHasMinimum
import com.softteco.template.utils.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val INPUT_DELAY: Long = 600

object ValidateFields {

    fun String.isEmailCorrect(): Boolean {
        return this.matches(Regex(Constants.EMAIL_PATTERN))
    }

    fun String.isHasMinimum(): Boolean {
        return this.matches(Regex(Constants.PASSWORD_PATTERN_MIN))
    }

    fun String.isHasCapitalizedLetter(): Boolean {
        return this.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))
    }
}

sealed class EmailFieldState {
    object Success : EmailFieldState()
    object Empty : EmailFieldState()
    object Waiting : EmailFieldState()
    object Error : EmailFieldState()
}

sealed class PasswordFieldState {
    object Success : PasswordFieldState()
    object Empty : PasswordFieldState()
    class Error(val isRightLength: Boolean, val isUppercase: Boolean) : PasswordFieldState()
}

@OptIn(FlowPreview::class)
internal fun validateEmail(
    fieldValue: MutableStateFlow<String>,
    fieldState: MutableStateFlow<EmailFieldState>,
    coroutineScope: CoroutineScope,
    appDispatchers: AppDispatchers
) {
    coroutineScope.launch(appDispatchers.ui) {
        fieldValue
            .onEach { fieldState.value = EmailFieldState.Waiting }
            .debounce(INPUT_DELAY.milliseconds).collect { value ->
                fieldState.value = when {
                    value.isEmailCorrect() -> EmailFieldState.Success
                    value.isEmpty() -> EmailFieldState.Empty
                    else -> EmailFieldState.Error
                }
            }
    }
}

internal fun validatePassword(password: String): PasswordFieldState {
    return when {
        password.isBlank() -> PasswordFieldState.Empty
        !password.isHasMinimum() || !password.isHasCapitalizedLetter() -> {
            PasswordFieldState.Error(
                isRightLength = password.isHasMinimum(),
                isUppercase = password.isHasCapitalizedLetter()
            )
        }

        else -> PasswordFieldState.Success
    }
}
