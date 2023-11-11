package com.softteco.template.ui.feature.login

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
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
class LoginViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `when login button is enabled and valid credentials success state is emitted`() = runTest {
        val credentials = CredentialsDto(email = "test@email.com", password = "password")
        coEvery { repository.login(credentials) } returns Result.Success(Unit)
        viewModel = LoginViewModel(repository)
        viewModel.state.test {
            awaitItem().onEmailChanged("test@email.com")
            awaitItem().onPasswordChanged("password")
            delay(1.seconds)
            expectMostRecentItem().run {
                isLoginBtnEnabled shouldBe true
                onLoginClicked()
            }
            awaitItem().run {
                loginState.shouldBeTypeOf<LoginViewModel.LoginState.Success>()
                snackBar.show shouldBe true
            }
        }
        coVerify(exactly = 1) { repository.login(credentials) }
    }

    @Test
    fun `when login button isn't enabled and invalid password then error state is emitted`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("test@email.com")
                awaitItem().onPasswordChanged("")
                delay(1.seconds)
                expectMostRecentItem().run {
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when login button isn't enabled and invalid email then error state is emitted`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("invalid@email")
                awaitItem().onPasswordChanged("password")
                delay(1.seconds)
                expectMostRecentItem().run {
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when login button isn't enabled with both empty email and password then error state is emitted`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().run {
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when login button clicked and request in progress then loading is shown`() = runTest {
        val credentials = CredentialsDto(email = "test@email.com", password = "password")
        coEvery { repository.login(credentials) } coAnswers {
            delay(2000)
            Result.Success(Unit)
        }
        viewModel = LoginViewModel(repository)
        viewModel.state.test {
            awaitItem().onEmailChanged("test@email.com")
            awaitItem().onPasswordChanged("password")
            delay(1.seconds)
            expectMostRecentItem().run {
                isLoginBtnEnabled shouldBe true
                onLoginClicked()
            }
            awaitItem().run {
                loginState.shouldBeTypeOf<LoginViewModel.LoginState.Loading>()
            }
        }
        coVerify(exactly = 1) { repository.login(credentials) }
    }
}
