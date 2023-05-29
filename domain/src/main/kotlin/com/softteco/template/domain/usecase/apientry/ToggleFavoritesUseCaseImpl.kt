package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.repository.ApisRepository
import javax.inject.Inject

internal class ToggleFavoritesUseCaseImpl @Inject constructor(
    private val apisRepository: ApisRepository
) : ToggleFavoritesUseCase {

    override suspend fun invoke(entry: ApiEntry) {
        val newEntry = entry.run { copy(favorite = !this.favorite) }
        apisRepository.updateEntry(newEntry)
    }
}
