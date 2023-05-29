package com.softteco.template.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softteco.template.domain.getDummyApiList
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApisUseCaseImplTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apisRepository: ApisRepository
    private lateinit var apisUseCase: FetchApiEntriesUseCase

    @Before
    fun setUp() {
        apisUseCase = FetchApiEntriesUseCaseImpl(apisRepository)
    }

    @Test
    fun `Given Apis When UseCase fetchApis returns Success`() = runBlocking {
        // GIVEN
        val inputFlow = flowOf(Output.success(getDummyApiList()))
        Mockito.`when`(apisRepository.fetchApiEntries()).thenReturn(inputFlow)
        // WHEN
        val output = apisUseCase.invoke().toList()
        // THEN
        assert(output[0].data?.size == 1)
    }
}
