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

private const val TOKEN = "testToken"
private const val NEW_PASSWORD = "newPassword"
private const val NEW_PASSWORD_NOT_VALID_1 = "newpassword"
private const val NEW_PASSWORD_NOT_VALID_2 = "neW"

@ExtendWith(MainDispatcherExtension::class)
class ResetPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: ResetPasswordViewModel

    @Test
    fun `when valid password and reset password button is enabled then success state is emitted`() =
        runTest {
            coEvery {
                repository.changePassword(TOKEN, NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD))
            } returns Result.Success(Unit)
            val savedStateHandle = SavedStateHandle().apply {
                set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
            }
            viewModel = ResetPasswordViewModel(repository, savedStateHandle, appDispatchers)

            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD)
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
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            }
        }

    @Test
    fun `when password hasn't capital letter then password field error is shown and button isn't enabled`() =
        runTest {
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                appDispatchers
            )
            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD_NOT_VALID_1)
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
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                appDispatchers
            )
            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD_NOT_VALID_2)
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
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                appDispatchers
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
            coEvery {
                repository.changePassword(
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            } coAnswers {
                delay(1.seconds)
                Result.Success(Unit)
            }
            viewModel = ResetPasswordViewModel(
                repository,
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                appDispatchers
            )

            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD)
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
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            }
        }
}
