package com.softteco.template.ui.feature.login

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.entity.AuthToken
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.ScreenState
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

private const val EMAIL = "test@email.com"
private const val PASSWORD = "Password"
private const val INVALID_EMAIL = "invalid@email"

@ExtendWith(MainDispatcherExtension::class)
class LoginViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: LoginViewModel
    private val snackbarController = SnackbarController()
    private val dialogController = DialogController()

    @Test
    fun `when valid credentials and login button is enabled then success state is emitted`() =
        runTest {
            val credentials = CredentialsDto(email = EMAIL, password = PASSWORD)
            coEvery {
                repository.login(credentials)
            } returns Result.Success(AuthToken("token_"))

            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                delay(1.seconds)
                expectMostRecentItem().run {
                    isLoginBtnEnabled shouldBe true
                    onLoginClicked()
                }
                awaitItem().screenState.shouldBeTypeOf<ScreenState.Success>()
            }
            coVerify(exactly = 1) { repository.login(credentials) }
        }

    @Test
    fun `when invalid password then login button isn't enabled and password field error is shown`() =
        runTest {
            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                delay(1.seconds)
                expectMostRecentItem().run {
                    passwordFieldState shouldBe TextFieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when invalid email then login button isn't enabled and email field error is shown`() =
        runTest {
            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().onEmailChanged(INVALID_EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onInputComplete(FieldType.EMAIL)
                delay(1.seconds)
                expectMostRecentItem().run {
                    emailFieldState shouldBe TextFieldState.EmailError(R.string.email_not_valid)
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when both empty email and password then button isn't enabled and email, password fields error are shown`() =
        runTest {
            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().run {
                    emailFieldState shouldBe TextFieldState.Empty
                    passwordFieldState shouldBe TextFieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when login button clicked and request in progress then loading is shown`() = runTest {
        val credentials = CredentialsDto(email = EMAIL, password = PASSWORD)
        coEvery { repository.login(credentials) } coAnswers {
            delay(1.seconds)
            Result.Success(AuthToken("token_"))
        }

        viewModel = LoginViewModel(
            repository,
            appDispatchers,
            snackbarController,
            dialogController
        )

        viewModel.state.test {
            awaitItem().onEmailChanged(EMAIL)
            awaitItem().onPasswordChanged(PASSWORD)
            delay(1.seconds)
            expectMostRecentItem().run {
                isLoginBtnEnabled shouldBe true
                onLoginClicked()
            }
            awaitItem().screenState.shouldBeTypeOf<ScreenState.Loading>()
        }
        coVerify(exactly = 1) { repository.login(credentials) }
    }
}
