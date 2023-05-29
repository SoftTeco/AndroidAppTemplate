package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.repository.ApisRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetApiEntryByNameUseCaseImpl @Inject constructor(
    private val apisRepository: ApisRepository
) : GetApiEntryByNameUseCase {

    override fun invoke(name: String): Flow<ApiEntry?> = apisRepository.apiEntry(name)
}
