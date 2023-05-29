package com.softteco.template.domain

import com.softteco.template.domain.model.ApiEntry

fun getDummyApiList() = listOf(
    ApiEntry(
        name = "test",
        auth = "test",
        category = "test",
        cors = "test",
        description = "test",
        https = true,
        link = "test",
        logo = "test",
        favorite = false,
    )
)
