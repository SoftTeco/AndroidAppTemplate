package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry

interface ToggleFavoritesUseCase {

    suspend operator fun invoke(entry: ApiEntry)
}
