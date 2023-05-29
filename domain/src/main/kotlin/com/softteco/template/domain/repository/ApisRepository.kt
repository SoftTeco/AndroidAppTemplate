package com.softteco.template.domain.repository

import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import kotlinx.coroutines.flow.Flow

/**
 * Interface of APIs Repository to expose to other module
 */
interface ApisRepository {

    val allEntries: Flow<List<ApiEntry>>

    val favorites: Flow<List<ApiEntry>>

    /**
     * Method to fetch the API entries from Repository
     * @return Flow of Outputs with [ApiEntry] list
     */
    fun fetchApiEntries(): Flow<Output<List<ApiEntry>>>

    suspend fun updateEntry(entry: ApiEntry)

    fun apiEntry(name: String): Flow<ApiEntry?>

    fun refresh()
}
