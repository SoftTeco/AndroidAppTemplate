package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.repository.ApisRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetAllApiEntriesUseCaseImpl @Inject constructor(
    private val apisRepository: ApisRepository
) : GetAllApiEntriesUseCase {

    override fun invoke(): Flow<List<ApiEntry>> = apisRepository.allEntries
}
