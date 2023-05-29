package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import kotlinx.coroutines.flow.Flow

/**
 * Interface of API Entries UseCase to expose to ui module
 */
interface FetchApiEntriesUseCase {
    /**
     * UseCase Method to fetch the API entries from Data Layer
     */
    operator fun invoke(): Flow<Output<List<ApiEntry>>>
}
