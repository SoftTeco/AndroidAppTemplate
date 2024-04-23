package com.softteco.template.ui.feature.signup

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.components.snackbar.SnackbarState
import com.softteco.template.ui.feature.onboarding.signup.SignUpViewModel
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

private const val USERNAME = "testuser"
private const val EMAIL = "test@email.com"
private const val PASSWORD = "passwordTest"
private const val INVALID_EMAIL = "invalid@email"
private const val NEW_PASSWORD_NOT_VALID_1 = "newpassword"
private const val NEW_PASSWORD_NOT_VALID_2 = "pswD"
private const val SELECTED_TERMS_CHECKBOX = true

@ExtendWith(MainDispatcherExtension::class)
class SignUpViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: SignUpViewModel
    private val snackbarController = SnackbarController()

    @Test
    fun `when sign up success then navigate to login`() {
        runTest {
            val createUserDto = CreateUserDto(
                username = USERNAME,
                email = EMAIL,
                password = PASSWORD
            )
            coEvery { repository.registration(createUserDto) } returns Result.Success("")

            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)

            val navDestination = async { viewModel.navDestination.first() }

            viewModel.state.test {
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe true
                    onRegisterClicked()
                }

                snackbarController.snackbars.value shouldContain SnackbarState(R.string.success)
            }

            launch { navDestination.await() shouldBe Screen.Login }

            coVerify(exactly = 1) { repository.registration(createUserDto) }
        }
    }

    @Test
    fun `when invalid email then email field error is shown and sign-up button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().onEmailChanged(INVALID_EMAIL)
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                awaitItem().onInputComplete(FieldType.EMAIL)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                    email.state.shouldBeTypeOf<FieldState.EmailError>()
                }
            }
        }
    }

    @Test
    fun `when password hasn't capital letter then password field error is shown and sign-up button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD_NOT_VALID_1)
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                awaitItem().onInputComplete(FieldType.PASSWORD)
                delay(1.seconds)

                expectMostRecentItem().run {
                    password.state.shouldBeTypeOf<FieldState.PasswordError>()
                    (password.state as FieldState.PasswordError).isUppercase shouldBe false
                    isSignupBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when password hasn't enough symbols then password field error is shown and sign-up button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().run {
                    onPasswordChanged(NEW_PASSWORD_NOT_VALID_2)
                    onInputComplete(FieldType.PASSWORD)
                }
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)

                expectMostRecentItem().run {
                    password.state.shouldBeTypeOf<FieldState.PasswordError>()
                    (password.state as FieldState.PasswordError).isRightLength shouldBe false
                    isSignupBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when empty username then sign-up button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when empty username, email, password then password, email fields error are shown and button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().run {
                    password.state shouldBe FieldState.Empty
                    email.state shouldBe FieldState.Empty
                    isSignupBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when checkbox isn't selected and sign-up button isn't enabled`() {
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)
            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().isSignupBtnEnabled shouldBe false
            }
        }
    }

    @Test
    fun `when registration button clicked and request in progress then loading is shown`() {
        runTest {
            val createUserDto = CreateUserDto(
                username = USERNAME,
                email = EMAIL,
                password = PASSWORD
            )
            coEvery { repository.registration(createUserDto) } coAnswers {
                delay(1.seconds)
                Result.Success("")
            }

            viewModel = SignUpViewModel(repository, appDispatchers, snackbarController)

            viewModel.state.test {
                awaitItem().onUsernameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe true
                    onRegisterClicked()
                }

                awaitItem().signUpState shouldBe SignUpViewModel.SignupState(
                    loading = true,
                    isCtaEnabled = true
                )
            }

            coVerify(exactly = 1) { repository.registration(createUserDto) }
        }
    }
}
