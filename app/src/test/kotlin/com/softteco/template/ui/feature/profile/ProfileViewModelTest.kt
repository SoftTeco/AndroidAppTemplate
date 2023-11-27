package com.softteco.template.ui.feature.profile

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.utils.MainDispatcherExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MainDispatcherExtension::class)
class ProfileViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var viewModel: ProfileViewModel

    @Test
    fun `when screen is open and data isn't received then loading is shown`() = runTest {
        coEvery { profileRepository.getUser() } coAnswers {
            delay(1.seconds) // emulation a delay in receiving data
            Result.Success(testProfile)
        }
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        viewModel.state.test {
            awaitItem().profileState shouldBe ProfileViewModel.GetProfileState.Loading
        }
    }

    @Test
    fun `when screen is open and data received then profile data is shown`() = runTest {
        coEvery { profileRepository.getUser() } returns Result.Success(testProfile)
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        viewModel.state.test {
            awaitItem().run {
                profileState.shouldBeTypeOf<ProfileViewModel.GetProfileState.Success>()
                (profileState as ProfileViewModel.GetProfileState.Success).profile shouldBe testProfile
            }
        }
        coVerify(exactly = 1) { profileRepository.getUser() }
    }

    @Test
    fun `when screen is open and network error happened then snackbar is shown`() = runTest {
        coEvery { profileRepository.getUser() } returns Result.Error(ErrorEntity.Network)
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        viewModel.state.test {
            awaitItem().snackbar.show shouldBe true
        }
        coVerify(exactly = 1) { profileRepository.getUser() }
    }

    companion object {
        private val testProfile = Profile(
            id = 1,
            username = "John",
            firstName = "John",
            lastName = "Doe",
            birthDate = "2023-10-30",
            email = "email@gmail.com",
            createdAt = "2023-10-30 06:58:31.108922",
        )
    }
}
