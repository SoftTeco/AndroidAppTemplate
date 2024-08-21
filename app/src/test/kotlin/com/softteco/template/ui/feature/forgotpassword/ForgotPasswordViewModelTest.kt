package com.softteco.template.ui.feature.forgotpassword

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.data.auth.dto.ResetPasswordDto
import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.base.error.Result
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.components.snackbar.SnackbarState
import com.softteco.template.ui.feature.onboarding.password.forgot.ForgotPasswordViewModel
import com.softteco.template.utils.MainDispatcherExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration.Companion.seconds

private const val EMAIL = "test@email.com"
private const val INVALID_EMAIL = "invalid@email"

@ExtendWith(MainDispatcherExtension::class)
class ForgotPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: ForgotPasswordViewModel
    private val snackbarController = SnackbarController()
    private val dialogController = DialogController()

    @Test
    fun `when reset password mail sent then navigate to login`() {
        runTest {
            coEvery { repository.resetPassword(ResetPasswordDto(EMAIL)) } returns Result.Success(
                Unit
            )

            viewModel = ForgotPasswordViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            val navDestination = async { viewModel.navDestination.first() }

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onRestorePasswordClicked()
                }

                snackbarController.snackbars.value shouldContain SnackbarState(R.string.check_email)

                launch {
                    navDestination.await() shouldBe Screen.Login
                }
            }

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(EMAIL)) }
        }
    }

    @Test
    fun `when invalid email then reset password button isn't enabled and email field error is shown`() {
        runTest {
            viewModel = ForgotPasswordViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().run {
                    onEmailChanged(INVALID_EMAIL)
                    onInputComplete()
                }

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe false
                    email.state shouldBe FieldState.EmailError(R.string.email_not_valid)
                }
            }
        }
    }

    @Test
    fun `when empty email then reset password button isn't enabled and email field error is shown`() {
        runTest {
            viewModel = ForgotPasswordViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().run {
                    isResetBtnEnabled shouldBe false
                    email.state shouldBe FieldState.Empty
                }
            }
        }
    }

    @Test
    fun `when reset password button clicked and request in progress then loading is shown`() {
        runTest {
            coEvery { repository.resetPassword(ResetPasswordDto(EMAIL)) } coAnswers {
                delay(1.seconds)
                Result.Success(Unit)
            }

            viewModel = ForgotPasswordViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onRestorePasswordClicked()
                }

                awaitItem().loading shouldBe true
            }

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(EMAIL)) }
        }
    }
}
