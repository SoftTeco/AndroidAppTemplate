package com.softteco.template.ui.feature.forgotPassword

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.ui.feature.EmailFieldState
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
private const val INVALID_EMAIL = "invalid@email"

@ExtendWith(MainDispatcherExtension::class)
class ForgotPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: ForgotPasswordViewModel

    @Test
    fun `when valid email and reset password button is enabled then success state is emitted`() =
        runTest {
            coEvery { repository.resetPassword(ResetPasswordDto(EMAIL)) } returns Result.Success(
                Unit
            )
            viewModel = ForgotPasswordViewModel(repository, appDispatchers)

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onRestorePasswordClicked()
                }

                awaitItem().run {
                    forgotPasswordState.shouldBeTypeOf<ForgotPasswordViewModel.ForgotPasswordState.Success>()
                    snackBar.show shouldBe true
                }
            }

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(EMAIL)) }
        }

    @Test
    fun `when invalid email then reset password button isn't enabled and email field error is shown`() =
        runTest {
            viewModel = ForgotPasswordViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().onEmailChanged(INVALID_EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe false
                    fieldStateEmail shouldBe EmailFieldState.Error
                }
            }
        }

    @Test
    fun `when empty email then reset password button isn't enabled and email field error is shown`() =
        runTest {
            viewModel = ForgotPasswordViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().run {
                    isResetBtnEnabled shouldBe false
                    fieldStateEmail shouldBe EmailFieldState.Empty
                }
            }
        }

    @Test
    fun `when reset password button clicked and request in progress then loading is shown`() =
        runTest {
            coEvery { repository.resetPassword(ResetPasswordDto(EMAIL)) } coAnswers {
                delay(1.seconds)
                Result.Success(Unit)
            }
            viewModel = ForgotPasswordViewModel(repository, appDispatchers)

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onRestorePasswordClicked()
                }

                awaitItem().run {
                    forgotPasswordState.shouldBeTypeOf<ForgotPasswordViewModel.ForgotPasswordState.Loading>()
                }
            }

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(EMAIL)) }
        }
}
