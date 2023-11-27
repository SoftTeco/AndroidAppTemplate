package com.softteco.template

import com.softteco.template.utils.AppDispatchers
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

open class BaseTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    protected val appDispatchers = AppDispatchers(dispatcher, dispatcher, dispatcher, dispatcher)
    init {
        MockKAnnotations.init(this)
    }
}
