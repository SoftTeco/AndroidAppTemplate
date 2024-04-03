package com.softteco.template.utils

import com.softteco.template.Events
import com.softteco.template.data.analytics.AnalyticsProvider

object Analytics {
    private var provider: AnalyticsProvider? = null

    fun init(provider: AnalyticsProvider) {
        this.provider = provider
    }

    fun homeOpened() {
        provider?.logEvent(Events.HOME_OPENED)
    }

    fun resetPasswordOpened() {
        provider?.logEvent(Events.RESET_PASSWORD_OPENED)
    }

    fun logInOpened() {
        provider?.logEvent(Events.LOG_IN_OPENED)
    }

    fun licensesOpened() {
        provider?.logEvent(Events.LICENSES_OPENED)
    }

    fun profileOpened() {
        provider?.logEvent(Events.PROFILE_OPENED)
    }

    fun settingsOpened() {
        provider?.logEvent(Events.SETTINGS_OPENED)
    }

    fun forgotPasswordOpened() {
        provider?.logEvent(Events.FORGOT_PASSWORD_OPENED)
    }

    fun signUpOpened() {
        provider?.logEvent(Events.SIGN_UP_OPENED)
    }

    fun signUpSuccess() {
        provider?.logEvent(Events.SIGN_UP_SUCCESS)
    }

    fun logInSuccess() {
        provider?.logEvent(Events.LOG_IN_SUCCESS)
    }

    fun resetPasswordSuccess() {
        provider?.logEvent(Events.RESET_PASSWORD_SUCCESS)
    }
}
