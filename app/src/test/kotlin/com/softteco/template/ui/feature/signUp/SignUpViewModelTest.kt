package com.softteco.template.ui.feature.signUp

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
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

private const val USERNAME = "testuser"
private const val EMAIL = "test@email.com"
private const val PASSWORD = "passwordTest"
private const val INVALID_EMAIL = "invalid@email"
private const val NEW_PASSWORD_NOT_VALID_1 = "newpassword"
private const val NEW_PASSWORD_NOT_VALID_2 = "pswD"
private const val SELECTED_TERMS_CHECKBOX = true
private const val UNSELECTED_TERMS_CHECKBOX = false

@ExtendWith(MainDispatcherExtension::class)
class SignUpViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: SignUpViewModel

    @Test
    fun `when valid credentials and sign-up button is enabled then success state is emitted`() =
        runTest {
            val createUserDto = CreateUserDto(
                username = USERNAME,
                email = EMAIL,
                password = PASSWORD
            )
            coEvery { repository.registration(createUserDto) } returns Result.Success("")
            viewModel = SignUpViewModel(repository, appDispatchers)

            viewModel.state.test {
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe true
                    onRegisterClicked()
                }

                awaitItem().run {
                    registrationState.shouldBeTypeOf<SignUpViewModel.SignupState.Success>()
                    snackBar.show shouldBe true
                }
            }

            coVerify(exactly = 1) { repository.registration(createUserDto) }
        }

    @Test
    fun `when invalid email then email field error is shown and sign-up button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().onEmailChanged(INVALID_EMAIL)
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                    fieldStateEmail shouldBe EmailFieldState.Error
                }
            }
        }

    @Test
    fun `when password hasn't capital letter then password field error is shown and sign-up button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD_NOT_VALID_1)
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    fieldStatePassword.shouldBeTypeOf<PasswordFieldState.Error>()
                    (fieldStatePassword as PasswordFieldState.Error).isUppercase shouldBe false
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when password hasn't enough symbols then password field error is shown and sign-up button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().onPasswordChanged(NEW_PASSWORD_NOT_VALID_2)
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    fieldStatePassword.shouldBeTypeOf<PasswordFieldState.Error>()
                    (fieldStatePassword as PasswordFieldState.Error).isRightLength shouldBe false
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when empty username then sign-up button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
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

    @Test
    fun `when empty username, email, password then password, email fields error are shown and button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().run {
                    fieldStatePassword shouldBe PasswordFieldState.Empty
                    fieldStateEmail shouldBe EmailFieldState.Empty
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when checkbox isn't selected and sign-up button isn't enabled`() =
        runTest {
            viewModel = SignUpViewModel(repository, appDispatchers)
            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(UNSELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when registration button clicked and request in progress then loading is shown`() =
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
            viewModel = SignUpViewModel(repository, appDispatchers)

            viewModel.state.test {
                awaitItem().onUserNameChanged(USERNAME)
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                awaitItem().onCheckTermsChange(SELECTED_TERMS_CHECKBOX)
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe true
                    onRegisterClicked()
                }

                awaitItem().run {
                    registrationState.shouldBeTypeOf<SignUpViewModel.SignupState.Loading>()
                }
            }

            coVerify(exactly = 1) { repository.registration(createUserDto) }
        }
}
