package com.softteco.template.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softteco.template.data.getDummyApiList
import com.softteco.template.data.source.local.ApiEntryDao
import com.softteco.template.data.source.remote.ApisRemoteDataSource
import com.softteco.template.data.source.remote.model.toDomainModel
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApisRepositoryTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var apisRepository: ApisRepository

    @Mock
    lateinit var apisRemoteDataSource: ApisRemoteDataSource

    @Mock
    lateinit var apiEntryDao: ApiEntryDao

    @Before
    fun setUp() {
        apisRepository = ApisRepositoryImpl(apisRemoteDataSource, apiEntryDao)
    }

    @Test
    fun `Given Apis When fetchApis returns Success`() = runBlocking {
        // GIVEN
        val givenApis = getDummyApiList()
        val givenApisOutput = Output.success(givenApis)
        val inputFlow = listOf(Output.loading(), givenApisOutput)
        `when`(apisRemoteDataSource.fetchEntries()).thenReturn(givenApisOutput)

        // WHEN
        val outputFlow = apisRepository.fetchApiEntries().toList()

        // THEN
        assert(outputFlow.size == 2)
        assert(inputFlow[0].status == outputFlow[0].status)
        assert(inputFlow[1].data!!.entries.map { it.toDomainModel() } == outputFlow[1].data)
    }
}
