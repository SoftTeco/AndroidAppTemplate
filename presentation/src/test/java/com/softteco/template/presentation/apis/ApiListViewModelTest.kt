package com.softteco.template.presentation.apis

import com.softteco.template.domain.model.Output
import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.GetAllApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.ToggleFavoritesUseCase
import com.softteco.template.presentation.BaseViewModelTest
import com.softteco.template.presentation.features.apis.ApiListViewModel
import com.softteco.template.presentation.getDummyApiList
import com.softteco.template.presentation.runBlockingMainTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApiListViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var fetchApiEntriesUseCase: FetchApiEntriesUseCase

    @Mock
    private lateinit var toggleFavoritesUseCase: ToggleFavoritesUseCase

    @Mock
    private lateinit var getAllApiEntriesUseCase: GetAllApiEntriesUseCase
    private lateinit var apiListViewModel: ApiListViewModel

    @Before
    fun setUp() {
        apiListViewModel = ApiListViewModel(
            getAllApiEntriesUseCase,
            fetchApiEntriesUseCase,
            toggleFavoritesUseCase
        )
    }

    @Test
    fun `Given Apis when fetchApis should return Success`() = runBlockingMainTest {
        // GIVEN
        val flowQuestions = flowOf(Output.success(getDummyApiList()))

        // WHEN
        Mockito.doReturn(flowQuestions).`when`(fetchApiEntriesUseCase).invoke()
        apiListViewModel.fetchAllApis()

        // THEN
        assert(1 == apiListViewModel.fetchApiListOutput.value?.data?.size)
    }
}
