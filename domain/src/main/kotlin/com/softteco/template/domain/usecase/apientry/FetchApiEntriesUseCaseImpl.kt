package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of API Entries UseCase
 * @param apisRepository the APIs repository object
 */
internal class FetchApiEntriesUseCaseImpl @Inject constructor(
    private val apisRepository: ApisRepository
) : FetchApiEntriesUseCase {

    override fun invoke(): Flow<Output<List<ApiEntry>>> {
        return apisRepository.fetchApiEntries()
    }
}
