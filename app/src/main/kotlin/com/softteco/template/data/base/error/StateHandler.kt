package com.softteco.template.data.base.error

sealed class StateHandler {
    object Loading : StateHandler()
    object Success : StateHandler()
    data class Error(val resultHandler: Unit) : StateHandler()
}
