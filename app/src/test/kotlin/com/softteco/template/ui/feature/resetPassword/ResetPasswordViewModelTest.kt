package com.softteco.template.ui.feature.resetPassword

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.navigation.AppNavHost
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
class ResetPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: ResetPasswordViewModel

    @Test
    fun `when valid password and reset password button is enabled then success state is emitted`() =
        runTest {
            val token = "testToken"
            val newPassword = "newPassword"
            coEvery {
                repository.changePassword(token, NewPasswordDto(newPassword, newPassword))
            } returns Result.Success(Unit)
            val savedStateHandle = SavedStateHandle().apply {
                set(AppNavHost.RESET_TOKEN_ARG, token)
            }
            viewModel = ResetPasswordViewModel(repository, savedStateHandle)

            viewModel.state.test {
                awaitItem().onPasswordChanged(newPassword)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onResetPasswordClicked()
                }

                awaitItem().run {
                    resetPasswordState.shouldBeTypeOf<ResetPasswordViewModel.ResetPasswordState.Success>()
                    snackBar.show shouldBe true
                }
            }

            coVerify(exactly = 1) {
                repository.changePassword(
                    token,
                    NewPasswordDto(newPassword, newPassword)
                )
            }
        }

    @Test
    fun `when password hasn't capital letter then password field error is shown and button isn't enabled`() =
        runTest {
            val token = "testToken"
            val newPassword = "newpassword"
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, token)
                }
            )
            viewModel.state.test {
                awaitItem().onPasswordChanged(newPassword)
                delay(1.seconds)
                expectMostRecentItem().run {
                    fieldStatePassword.shouldBeTypeOf<PasswordFieldState.Error>()
                    (fieldStatePassword as PasswordFieldState.Error).isUppercase shouldBe false
                    isResetBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when password hasn't enough letters then password field error is shown and button isn't enabled`() =
        runTest {
            val token = "testToken"
            val newPassword = "neW"
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, token)
                }
            )
            viewModel.state.test {
                awaitItem().onPasswordChanged(newPassword)
                delay(1.seconds)
                expectMostRecentItem().run {
                    fieldStatePassword.shouldBeTypeOf<PasswordFieldState.Error>()
                    (fieldStatePassword as PasswordFieldState.Error).isRightLength shouldBe false
                    isResetBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when empty password then password field error is shown and button isn't enabled`() =
        runTest {
            val token = "testToken"
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, token)
                }
            )
            viewModel.state.test {
                awaitItem().run {
                    isResetBtnEnabled shouldBe false
                    fieldStatePassword shouldBe PasswordFieldState.Empty
                }
            }
        }

    @Test
    fun `when reset password button clicked and request in progress then loading is shown`() =
        runTest {
            val token = "testToken"
            val newPassword = "newPassword"
            coEvery {
                repository.changePassword(
                    token,
                    NewPasswordDto(newPassword, newPassword)
                )
            } coAnswers {
                delay(1.seconds)
                Result.Success(Unit)
            }
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, token)
                }
            )

            viewModel.state.test {
                awaitItem().onPasswordChanged(newPassword)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onResetPasswordClicked()
                }

                awaitItem().run {
                    resetPasswordState.shouldBeTypeOf<ResetPasswordViewModel.ResetPasswordState.Loading>()
                }
            }

            coVerify(exactly = 1) {
                repository.changePassword(
                    token,
                    NewPasswordDto(newPassword, newPassword)
                )
            }
        }
}
