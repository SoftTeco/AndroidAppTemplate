package com.softteco.template.ui.feature.signUp

import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
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
class SignUpViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: SignUpViewModel

    @Test
    fun `when sign-up button is enabled and valid credentials then success state is emitted`() =
        runTest {
            val createUserDto = CreateUserDto(
                username = "testuser",
                email = "test@email.com",
                password = "passwordTest"
            )
            coEvery { repository.registration(createUserDto) } returns Result.Success("")
            viewModel = SignUpViewModel(repository)

            viewModel.state.test {
                awaitItem().onUserNameChanged("testuser")
                awaitItem().onEmailChanged("test@email.com")
                awaitItem().onPasswordChanged("passwordTest")
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
    fun `when sign-up button isn't enabled and invalid email then error state is emitted`() =
        runTest {
            viewModel = SignUpViewModel(repository)
            viewModel.state.test {
                awaitItem().onEmailChanged("invalid@email")
                awaitItem().onUserNameChanged("testuser")
                awaitItem().onPasswordChanged("passwordTest")
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when sign-up button isn't enabled and invalid password then error state is emitted`() =
        runTest {
            viewModel = SignUpViewModel(repository)
            viewModel.state.test {
                awaitItem().onPasswordChanged("password")
                awaitItem().onUserNameChanged("testuser")
                awaitItem().onEmailChanged("test@email.com")
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when sign-up button isn't enabled and invalid password has not enough symbols then error state is emitted`() =
        runTest {
            viewModel = SignUpViewModel(repository)
            viewModel.state.test {
                awaitItem().onPasswordChanged("pswD")
                awaitItem().onUserNameChanged("testuser")
                awaitItem().onEmailChanged("test@email.com")
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when sign-up button isn't enabled and invalid username then error state is emitted`() =
        runTest {
            viewModel = SignUpViewModel(repository)
            viewModel.state.test {
                awaitItem().onPasswordChanged("passwordTest")
                awaitItem().onEmailChanged("test@email.com")
                delay(1.seconds)

                expectMostRecentItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when sign-up button isn't enabled and empty username, email, and password then error state is emitted`() =
        runTest {
            viewModel = SignUpViewModel(repository)
            viewModel.state.test {
                awaitItem().run {
                    isSignupBtnEnabled shouldBe false
                }
            }
        }

    @Test
    fun `when registration button clicked and request in progress then loading is shown`() =
        runTest {
            val createUserDto = CreateUserDto(
                username = "testuser",
                email = "test@email.com",
                password = "passwordTest"
            )
            coEvery { repository.registration(createUserDto) } coAnswers {
                delay(2000)
                Result.Success("")
            }
            viewModel = SignUpViewModel(repository)

            viewModel.state.test {
                awaitItem().onUserNameChanged("testuser")
                awaitItem().onEmailChanged("test@email.com")
                awaitItem().onPasswordChanged("passwordTest")
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
