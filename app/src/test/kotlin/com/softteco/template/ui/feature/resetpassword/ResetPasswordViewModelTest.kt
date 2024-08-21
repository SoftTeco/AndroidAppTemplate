package com.softteco.template.ui.feature.resetpassword

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.data.auth.dto.NewPasswordDto
import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.base.error.Result
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.components.snackbar.SnackbarState
import com.softteco.template.ui.feature.onboarding.password.reset.ResetPasswordViewModel
import com.softteco.template.utils.MainDispatcherExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
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

private const val TOKEN = "testToken"
private const val NEW_PASSWORD = "newPassword"
private const val NEW_PASSWORD_NOT_VALID_1 = "newpassword"
private const val NEW_PASSWORD_NOT_VALID_2 = "neW"

@ExtendWith(MainDispatcherExtension::class)
class ResetPasswordViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: ResetPasswordViewModel
    private val snackbarController = SnackbarController()

    @Test
    fun `when password reset success then navigate to login`() {
        runTest {
            coEvery {
                authRepository.changePassword(TOKEN, NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD))
            } returns Result.Success(Unit)

            val savedStateHandle = SavedStateHandle().apply {
                set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
            }

            viewModel = ResetPasswordViewModel(
                savedStateHandle,
                authRepository,
                appDispatchers,
                snackbarController
            )

            val navDestination = async { viewModel.navDestination.first() }

            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onResetPasswordClicked()
                }

                snackbarController.snackbars.value shouldContain SnackbarState(R.string.success)
            }

            launch {
                navDestination.await() shouldBe Screen.Login
            }

            coVerify(exactly = 1) {
                authRepository.changePassword(
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            }
        }
    }

    @Test
    fun `when password hasn't capital letter then password field error is shown and button isn't enabled`() {
        runTest {
            viewModel = ResetPasswordViewModel(
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                authRepository,
                appDispatchers,
                snackbarController,
            )

            viewModel.state.test {
                awaitItem().run {
                    onPasswordChanged(NEW_PASSWORD_NOT_VALID_1)
                    onInputComplete()
                }
                expectMostRecentItem().run {
                    password.state.shouldBeTypeOf<FieldState.PasswordError>()
                    (password.state as FieldState.PasswordError).isUppercase shouldBe false
                    isResetBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when password hasn't enough letters then password field error is shown and button isn't enabled`() {
        runTest {
            viewModel = ResetPasswordViewModel(
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                authRepository,
                appDispatchers,
                snackbarController,
            )

            viewModel.state.test {
                awaitItem().run {
                    onPasswordChanged(NEW_PASSWORD_NOT_VALID_2)
                    onInputComplete()
                }
                expectMostRecentItem().run {
                    password.state.shouldBeTypeOf<FieldState.PasswordError>()
                    (password.state as FieldState.PasswordError).isRightLength shouldBe false
                    isResetBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when empty password then password field error is shown and button isn't enabled`() {
        runTest {
            viewModel = ResetPasswordViewModel(
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                authRepository,
                appDispatchers,
                snackbarController,
            )

            viewModel.state.test {
                awaitItem().run {
                    isResetBtnEnabled shouldBe false
                    password.state shouldBe FieldState.Empty
                }
            }
        }
    }

    @Test
    fun `when reset password button clicked and request in progress then loading is shown`() {
        runTest {
            coEvery {
                authRepository.changePassword(
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            } coAnswers {
                delay(1.seconds)
                Result.Success(Unit)
            }

            viewModel = ResetPasswordViewModel(
                SavedStateHandle().apply {
                    set(AppNavHost.RESET_TOKEN_ARG, TOKEN)
                },
                authRepository,
                appDispatchers,
                snackbarController,
            )

            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isResetBtnEnabled shouldBe true
                    onResetPasswordClicked()
                }

                awaitItem().loading shouldBe true
            }

            coVerify(exactly = 1) {
                authRepository.changePassword(
                    TOKEN,
                    NewPasswordDto(NEW_PASSWORD, NEW_PASSWORD)
                )
            }
        }
    }
}
