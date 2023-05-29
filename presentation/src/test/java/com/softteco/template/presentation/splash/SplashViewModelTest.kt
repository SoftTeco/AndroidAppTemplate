package com.softteco.template.presentation.splash

import androidx.lifecycle.asLiveData
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import com.softteco.template.presentation.BaseViewModelTest
import com.softteco.template.presentation.features.splash.SplashViewModel
import com.softteco.template.presentation.observeForTesting
import com.softteco.template.presentation.runBlockingMainTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest : BaseViewModelTest() {

    private lateinit var splashViewModel: SplashViewModel

    @Mock
    private lateinit var apisRepository: ApisRepository

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(apisRepository)
    }

    @Test
    fun `Given output When load returns Success`() = runBlockingMainTest {
        // WHEN
        splashViewModel.load()

        // THEN
        splashViewModel.isOk.asLiveData().observeForTesting {
            assert(splashViewModel.isOk.value.status == Output.Status.LOADING)
        }
    }
}
