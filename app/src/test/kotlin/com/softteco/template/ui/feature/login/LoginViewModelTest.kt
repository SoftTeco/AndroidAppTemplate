package com.softteco.template.ui.feature.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import com.softteco.template.BaseTest
import com.softteco.template.MainViewModel
import com.softteco.template.R
import com.softteco.template.data.RestCountriesApi
import com.softteco.template.data.TemplateApi
import com.softteco.template.data.base.ApiSuccess
import com.softteco.template.data.base.error.AppError
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.ProfileRepositoryImpl
import com.softteco.template.data.profile.dto.AuthTokenDto
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.toModel
import com.softteco.template.data.profile.entity.AuthToken
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.onboarding.login.LoginViewModel
import com.softteco.template.utils.MainDispatcherExtension
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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

    @RelaxedMockK
    private lateinit var tokenStore: DataStore<AuthTokenDto>

    @RelaxedMockK
    private lateinit var profileStore: DataStore<ProfileDto>

    @RelaxedMockK
    private lateinit var preferencesStore: DataStore<Preferences>

    @MockK
    private lateinit var templateApi: TemplateApi

    @MockK
    private lateinit var restCountriesApi: RestCountriesApi

    @Test
    fun `when login success then data saved to data store`() {
        runTest {
            val credentials = CredentialsDto(email = EMAIL, password = PASSWORD)
            val authTokenDto = AuthTokenDto("_token")

            val repository = ProfileRepositoryImpl(
                templateApi,
                tokenStore,
                profileStore,
                restCountriesApi
            )

            coEvery {
                tokenStore.updateData { authTokenDto }
            } returns authTokenDto

            coEvery {
                templateApi.login(credentials)
            } returns ApiSuccess(200, AuthTokenDto("token"))

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
                cancelAndConsumeRemainingEvents()
            }

            coVerify(exactly = 1) { tokenStore.updateData(any()) }
            coVerify(exactly = 1) { templateApi.login(credentials) }
        }
    }

    @Test
    fun `when auth data saved to datastore then user is logged in`() {
        runTest {
            val authTokenDto = AuthTokenDto("_token")
            val profile = ProfileDto(1, username = "test", email = "", createdAt = "")

            val repository = ProfileRepositoryImpl(
                templateApi,
                tokenStore,
                profileStore,
                restCountriesApi
            )

            coEvery {
                tokenStore.data
            } returns flowOf(authTokenDto)

            coEvery {
                templateApi.getUser(authTokenDto.toModel().composeHeader())
            } returns ApiSuccess(200, profile)

            coEvery {
                profileStore.data
            } returns flowOf(profile)

            val mainViewModel = MainViewModel(
                dataStore = preferencesStore,
                appDispatchers = appDispatchers,
                profileRepository = repository,
            )

            mainViewModel.isUserLoggedIn.test {
                awaitItem() shouldBe true
            }

            coVerify(exactly = 1) { tokenStore.data }
            coVerify(exactly = 1) { profileStore.data }
            coVerify(exactly = 1) { templateApi.getUser(any()) }
        }
    }

    @Test
    fun `when invalid password then login button isn't enabled and password field error is shown`() {
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
                    password.state shouldBe FieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when invalid email then login button isn't enabled and email field error is shown`() {
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
                    email.state shouldBe FieldState.EmailError(R.string.email_not_valid)
                    isLoginBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when both empty email and password then button isn't enabled and email, password fields error are shown`() {
        runTest {
            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            viewModel.state.test {
                awaitItem().run {
                    email.state shouldBe FieldState.Empty
                    password.state shouldBe FieldState.Empty
                    isLoginBtnEnabled shouldBe false
                }
            }
        }
    }

    @Test
    fun `when login button clicked and request in progress then loading is shown`() {
        runTest {
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
                awaitItem().loading shouldBe true
            }
            coVerify(exactly = 1) { repository.login(credentials) }
        }
    }

    @Test
    fun `when sign dialog shown and sign up button clicked then navigate to sign up`() {
        runTest {
            val credentials = CredentialsDto(email = EMAIL, password = PASSWORD)
            coEvery {
                repository.login(credentials)
            } returns Result.Error(AppError.AuthError.EmailNotExist)

            viewModel = LoginViewModel(
                repository,
                appDispatchers,
                snackbarController,
                dialogController
            )

            val navDestination = async { viewModel.navDestination.first() }

            viewModel.state.test {
                awaitItem().onEmailChanged(EMAIL)
                awaitItem().onPasswordChanged(PASSWORD)
                delay(1.seconds)
                expectMostRecentItem().run {
                    isLoginBtnEnabled shouldBe true
                    onLoginClicked()
                }
                cancelAndConsumeRemainingEvents()
                dialogController.dialogs.value.first().positiveBtnAction?.invoke()
            }

            launch { navDestination.await() shouldBe Screen.SignUp }

            coVerify(exactly = 1) { repository.login(credentials) }
        }
    }
}
