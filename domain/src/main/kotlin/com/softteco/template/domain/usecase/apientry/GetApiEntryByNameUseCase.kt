package com.softteco.template.domain.usecase.apientry

import com.softteco.template.domain.model.ApiEntry
import kotlinx.coroutines.flow.Flow

interface GetApiEntryByNameUseCase {

    operator fun invoke(name: String): Flow<ApiEntry?>
}
