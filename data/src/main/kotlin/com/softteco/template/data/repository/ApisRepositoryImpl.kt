package com.softteco.template.data.repository

import com.softteco.template.data.repository.base.BaseRepository
import com.softteco.template.data.source.local.ApiEntryDao
import com.softteco.template.data.source.local.model.ApiEntryEntity
import com.softteco.template.data.source.local.model.toDomainModel
import com.softteco.template.data.source.remote.ApisRemoteDataSource
import com.softteco.template.data.source.remote.model.toDomainModel
import com.softteco.template.data.source.remote.model.toEntity
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Implementation of ApisRepository
 * @param apisRemoteDataSource the object of remote data source
 * @param apiEntryDao the object of internal data base
 */
internal class ApisRepositoryImpl @Inject constructor(
    private val apisRemoteDataSource: ApisRemoteDataSource,
    private val apiEntryDao: ApiEntryDao,
) : ApisRepository, BaseRepository() {

    override val allEntries: Flow<List<ApiEntry>> = apiEntryDao.allApiEntries()
        .map { list -> list.map { it.toDomainModel() } }

    override val favorites: Flow<List<ApiEntry>> = apiEntryDao.favourites()
        .map { list -> list.map { it.toDomainModel() } }

    override fun fetchApiEntries(): Flow<Output<List<ApiEntry>>> {
        return flow {
            emit(Output.loading())
            val response = apisRemoteDataSource.fetchEntries()
            response.data?.entries?.map { it.toEntity() }?.let { data ->
                apiEntryDao.replaceDataset(data)
            }
            emit(
                Output(
                    response.status,
                    response.data?.entries?.map { it.toDomainModel() },
                    response.error,
                    response.message
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun apiEntry(name: String) = apiEntryDao.apiEntry(name).map { it?.toDomainModel() }

    override suspend fun updateEntry(entry: ApiEntry) =
        apiEntryDao.insertOne(ApiEntryEntity.fromApiEntry(entry))

    override fun refresh() {
        launch {
            val response = apisRemoteDataSource.fetchEntries()
            if (response.status != Output.Status.SUCCESS) {
                return@launch
            }
            apiEntryDao.replaceDataset(
                response.data?.entries?.map { it.toEntity() } ?: emptyList(),
            )
        }
    }
}
