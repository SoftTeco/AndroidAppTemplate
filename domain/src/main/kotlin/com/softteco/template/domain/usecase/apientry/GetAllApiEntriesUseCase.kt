package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import kotlinx.coroutines.flow.Flow

interface GetAllApiEntriesUseCase {

    operator fun invoke(): Flow<List<ApiEntry>>
}
