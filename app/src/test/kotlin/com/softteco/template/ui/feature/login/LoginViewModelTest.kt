package com.softteco.template.ui.feature.login

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
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
    fun `when valid credentials and login button is enabled then success state is emitted`() = runTest {
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
    fun `when invalid password then login button isn't enabled and password field error is shown`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("test@email.com")
                delay(1.seconds)
                expectMostRecentItem().run {
                    fieldStatePassword shouldBe PasswordFieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when invalid email then login button isn't enabled and email field error is shown`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("invalid@email")
                awaitItem().onPasswordChanged("password")
                delay(1.seconds)
                expectMostRecentItem().run {
                    fieldStateEmail shouldBe EmailFieldState.Error
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when both empty email and password then button isn't enabled and email, password fields error are shown`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.test {
                awaitItem().run {
                    fieldStateEmail shouldBe EmailFieldState.Empty
                    fieldStatePassword shouldBe PasswordFieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when login button clicked and request in progress then loading is shown`() = runTest {
        val credentials = CredentialsDto(email = "test@email.com", password = "password")
        coEvery { repository.login(credentials) } coAnswers {
            delay(1.seconds)
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
