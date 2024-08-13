package com.softteco.template.ui.feature.navigation.androidauto

import androidx.car.app.CarAppService
import androidx.car.app.validation.HostValidator

class MainCarAppService : CarAppService() {
    override fun createHostValidator() = HostValidator.ALLOW_ALL_HOSTS_VALIDATOR

    override fun onCreateSession() = MainCarSession()
}
