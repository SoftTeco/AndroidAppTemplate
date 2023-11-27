package com.softteco.template.ui.feature.profile

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.ui.theme.AppTheme
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest : BaseTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @RelaxedMockK
    private lateinit var profileRepository: ProfileRepository

    private lateinit var viewModel: ProfileViewModel

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun when_name_filed_clicked_then_edit_name_dialog_is_shown() = runTest {
        coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        composeTestRule.run {
            setContent {
                AppTheme {
                    ProfileScreen(
                        onBackClicked = {},
                        onLogout = {},
                        viewModel = viewModel,
                    )
                }
            }

            onNodeWithText(context.getString(R.string.full_name_title), useUnmergedTree = true)
                .onParent()
                .performClick()

            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText(context.getString(R.string.edit_name_dialog_title)).assertIsDisplayed()
        }
    }

    @Test
    fun when_birth_date_field_clicked_then_edit_birth_date_dialog_is_shown() = runTest {
        coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        composeTestRule.run {
            setContent {
                AppTheme {
                    ProfileScreen(
                        onBackClicked = {},
                        onLogout = {},
                        viewModel = viewModel,
                    )
                }
            }

            onNodeWithText(context.getString(R.string.birth_date_title)).onParent().performClick()

            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("Select date").assertIsDisplayed()
        }
    }

    @Test
    fun when_country_field_clicked_then_edit_country_dialog_is_shown() = runTest {
        coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
        viewModel = ProfileViewModel(profileRepository, appDispatchers)

        composeTestRule.run {
            setContent {
                AppTheme {
                    ProfileScreen(
                        onBackClicked = {},
                        onLogout = {},
                        viewModel = viewModel,
                    )
                }
            }

            onNodeWithText(context.getString(R.string.country_title), useUnmergedTree = true)
                .onParent()
                .performClick()

            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText(context.getString(R.string.change_country_dialog_title))
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_entering_characters_other_than_letters_text_field_value_does_not_change() {
        runTest {
            coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
            viewModel = ProfileViewModel(profileRepository, appDispatchers)

            composeTestRule.run {
                setContent {
                    AppTheme {
                        ProfileScreen(
                            onBackClicked = {},
                            onLogout = {},
                            viewModel = viewModel,
                        )
                    }
                }

                onNodeWithText(context.getString(R.string.full_name_title), useUnmergedTree = true)
                    .onParent()
                    .performClick()

                onNode(isFocused()).performTextInput("${testProfile.firstName} $42")

                onNode(isFocused()).assertTextContains(testProfile.fullName())
            }
        }
    }

    @Test
    fun when_entering_values_with_lowercase_letters_and_confirm_then_capitalized_values_will_be_saved() {
        runTest {
            coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
            viewModel = ProfileViewModel(profileRepository, appDispatchers)

            composeTestRule.run {
                setContent {
                    AppTheme {
                        ProfileScreen(
                            onBackClicked = {},
                            onLogout = {},
                            viewModel = viewModel,
                        )
                    }
                }

                onNodeWithText(context.getString(R.string.full_name_title), useUnmergedTree = true)
                    .onParent()
                    .performClick()

                onNode(isFocused()).performTextClearance()
                onNode(isFocused()).performTextInput(testProfile.fullName().lowercase())
                onNodeWithText(context.getString(R.string.ok)).onParent().performClick()

                onAllNodes(!isFocused() and hasText(context.getString(R.string.full_name_title)))
                    .assertAny(hasText(testProfile.fullName()))
            }
        }
    }

    @Test
    fun when_entering_more_than_two_values_and_confirm_then_first_two_values_will_be_saved() {
        runTest {
            coEvery { profileRepository.getUser() } coAnswers { Result.Success(testProfile) }
            viewModel = ProfileViewModel(profileRepository, appDispatchers)

            composeTestRule.run {
                setContent {
                    AppTheme {
                        ProfileScreen(
                            onBackClicked = {},
                            onLogout = {},
                            viewModel = viewModel,
                        )
                    }
                }

                onNodeWithText(context.getString(R.string.full_name_title), useUnmergedTree = true)
                    .onParent()
                    .performClick()

                onNode(isFocused()).performTextInput("${testProfile.fullName()} ${testProfile.username}")
                onNodeWithText(context.getString(R.string.ok)).onParent().performClick()

                onAllNodes(!isFocused() and hasText(context.getString(R.string.full_name_title)))
                    .assertAny(hasText(testProfile.fullName()))
            }
        }
    }

    companion object {
        private val testProfile = com.softteco.template.data.profile.entity.Profile(
            id = 1,
            username = "John",
            firstName = "John",
            lastName = "Doe",
            birthDate = "2023-10-30",
            email = "email@gmail.com",
            createdAt = "2023-10-30 06:58:31.108922",
        )
    }
}
