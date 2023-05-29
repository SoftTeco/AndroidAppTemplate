package com.softteco.template.data

import com.softteco.template.data.source.remote.model.ApiEntryApiModel
import com.softteco.template.data.source.remote.model.ApiEntryApiModels

fun getDummyApiList() = ApiEntryApiModels(
    count = 1,
    entries = listOf(
        ApiEntryApiModel(
            name = "test",
            auth = "test",
            category = "test",
            cors = "test",
            description = "test",
            https = true,
            link = "test",
        )
    )
)
