package com.softteco.template.ui.feature.forgotPassword

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ResetPasswordDto
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
class ForgotPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: ForgotPasswordViewModel

    @Test
    fun `when reset password button is enabled and valid email success state is emitted`() =
        runTest {
            val email = "test@email.com"
            coEvery { repository.resetPassword(ResetPasswordDto(email)) } returns Result.Success(
                Unit
            )
            viewModel = ForgotPasswordViewModel(repository)

            viewModel.state.test {
                awaitItem().onEmailChanged(email)
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

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(email)) }
        }

    @Test
    fun `when reset password button isn't enabled and invalid email then error state is emitted`() =
        runTest {
            viewModel = ForgotPasswordViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("invalid@email")
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when reset password button isn't enabled and empty email then error state is emitted`() =
        runTest {
            viewModel = ForgotPasswordViewModel(repository)
            viewModel.state.test {
                awaitItem().run {
                    isResetBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when reset password button clicked and request in progress then loading is shown`() =
        runTest {
            val email = "test@email.com"
            coEvery { repository.resetPassword(ResetPasswordDto(email)) } coAnswers {
                delay(2000)
                Result.Success(Unit)
            }
            viewModel = ForgotPasswordViewModel(repository)

            viewModel.state.test {
                awaitItem().onEmailChanged(email)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onRestorePasswordClicked()
                }

                awaitItem().run {
                    forgotPasswordState.shouldBeTypeOf<ForgotPasswordViewModel.ForgotPasswordState.Loading>()
                }
            }

            coVerify(exactly = 1) { repository.resetPassword(ResetPasswordDto(email)) }
        }
}
