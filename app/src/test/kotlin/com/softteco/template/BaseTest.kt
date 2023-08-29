package com.softteco.template

import io.mockk.MockKAnnotations

open class BaseTest {
    init {
        MockKAnnotations.init(this)
    }
}
