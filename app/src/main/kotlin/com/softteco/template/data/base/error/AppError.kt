package com.softteco.template.data.base.error

import com.softteco.template.R

sealed class AppError(val messageRes: Int) {

    class NetworkError(messageRes: Int = R.string.error_network) : AppError(messageRes)

    class UnknownError(messageRes: Int = R.string.error_unknown) : AppError(messageRes)

    sealed class AuthError(val code: String, messageRes: Int) : AppError(messageRes) {
        data object WrongCredentials :
            AuthError("wrong_credentials", R.string.error_wrong_credentials)

        data object InvalidUsername :
            AuthError("invalid_username", R.string.error_invalid_username)

        data object InvalidEmail : AuthError("invalid_email", R.string.error_invalid_email)
        data object InvalidPassword :
            AuthError("invalid_password", R.string.error_invalid_password)

        data object InvalidToken :
            AuthError("invalid_token", R.string.error_invalid_access_token)

        data object UnavailableUsername : AuthError(
            "unavailable_username",
            R.string.error_username_unavailable
        )

        data object EmailInUse : AuthError("email_in_use", R.string.error_email_already_exists)
        data object EmailNotExist : AuthError("email_not_exist", R.string.error_email_not_found)
        data object UnconfirmedUser : AuthError("unconfirmed_user", R.string.error_unconfirmed_user)

        companion object {
            private fun values() = listOf(
                WrongCredentials,
                InvalidUsername,
                InvalidEmail,
                InvalidPassword,
                InvalidToken,
                UnavailableUsername,
                EmailInUse,
                EmailNotExist,
                UnconfirmedUser,
            )

            fun findByCode(code: String?): AppError {
                return values().find { it.code == code } ?: UnknownError()
            }
        }
    }

    sealed class LocalStorageAppError(messageRes: Int) : AppError(messageRes) {
        data object AuthTokenNotFound : LocalStorageAppError(R.string.error_invalid_access_token)
    }
}
