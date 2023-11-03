package com.softteco.template.ui.feature.login

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.utils.MainDispatcherExtension
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class LoginViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository

    private lateinit var viewModel: LoginViewModel

    @Test
    fun `when login is clicked with valid email and password, success state is shown`() = runTest {
        coEvery { repository.login(any()) } returns Result.Success(Unit)
        viewModel = LoginViewModel(repository)
        viewModel.state.value.onEmailChanged("test@example.com")
        viewModel.state.value.onPasswordChanged("password")

        viewModel.state.value.onLoginClicked()

        viewModel.state.test {
            awaitItem().run {
                loginState shouldBe LoginViewModel.LoginState.Success
                snackBar.show shouldBe true
            }
        }

        coVerify(exactly = 1) { repository.login(any()) }
    }

    @Test
    fun `when login is clicked with invalid email, error state is shown`() = runTest {
        viewModel = LoginViewModel(repository)
        viewModel.state.value.onEmailChanged("invalid-email")
        viewModel.state.value.onPasswordChanged("password")

        viewModel.state.value.onLoginClicked()

        viewModel.state.test {
            awaitItem().run {
                loginState shouldBe LoginViewModel.LoginState.Default
                snackBar.show shouldBe true
            }
        }

        coVerify(exactly = 0) { repository.login(any()) }
    }

    @Test
    fun `when login is clicked with empty password, error state is shown`() = runTest {
        viewModel = LoginViewModel(repository)
        viewModel.state.value.onEmailChanged("test@example.com")
        viewModel.state.value.onPasswordChanged("")

        viewModel.state.value.onLoginClicked()

        viewModel.state.test {
            awaitItem().run {
                loginState shouldBe LoginViewModel.LoginState.Default
                snackBar.show shouldBe true
            }
        }

        coVerify(exactly = 0) { repository.login(any()) }
    }

    @Test
    fun `when login is clicked with both empty email and password, error state is shown`() =
        runTest {
            viewModel = LoginViewModel(repository)
            viewModel.state.value.onEmailChanged("")
            viewModel.state.value.onPasswordChanged("")

            viewModel.state.value.onLoginClicked()

            viewModel.state.test {
                awaitItem().run {
                    loginState shouldBe LoginViewModel.LoginState.Default
                    snackBar.show shouldBe true
                }
            }

            coVerify(exactly = 0) { repository.login(any()) }
        }
}
