package com.softteco.template.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.platform.app.InstrumentationRegistry
import com.softteco.template.BaseTest
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditTextDialogTest : BaseTest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun when_dialog_is_appeared_then_text_field_is_focused() = runTest {
        val initialValue = "text"

        composeTestRule.run {
            setContent {
                var textFieldValue by rememberTextFieldValue(initialValue)
                AppTheme {
                    EditTextDialog(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        onDismiss = {}
                    )
                }
            }

            onNodeWithText(initialValue).assertIsFocused()
        }
    }

    @Test
    fun when_confirm_button_clicked_then_field_value_passed_to_onDismiss() = runTest {
        val initialValue = "text"

        composeTestRule.run {
            setContent {
                var textFieldValue by rememberTextFieldValue(initialValue)
                AppTheme {
                    EditTextDialog(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        onDismiss = { value ->
                            assertThat(value, equalTo(initialValue))
                        }
                    )
                }
            }

            onNodeWithText(context.getString(R.string.ok)).onParent().performClick()
        }
    }

    @Test
    fun when_text_changed_and_confirm_button_clicked_then_field_value_passed_to_onDismiss() =
        runTest {
            val initialValue = "text"
            val newValue = "new text"

            composeTestRule.run {
                setContent {
                    var textFieldValue by rememberTextFieldValue(initialValue)
                    AppTheme {
                        EditTextDialog(
                            value = textFieldValue,
                            onValueChange = { textFieldValue = it },
                            onDismiss = { value ->
                                assertThat(value, equalTo(newValue))
                            }
                        )
                    }
                }

                onNodeWithText(initialValue).performTextInput(newValue)
                onNodeWithText(context.getString(R.string.ok)).onParent().performClick()
            }
        }

    @Test
    fun when_dismiss_button_clicked_then_null_passed_to_onDismiss() = runTest {
        val initialValue = "text"

        composeTestRule.run {
            setContent {
                var textFieldValue by rememberTextFieldValue(initialValue)

                AppTheme {
                    EditTextDialog(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        onDismiss = { value ->
                            assertThat(value, equalTo(null))
                        }
                    )
                }
            }

            onNodeWithText(context.getString(R.string.ok)).onParent().performClick()
        }
    }

    @Composable
    private fun rememberTextFieldValue(initial: String): MutableState<TextFieldValue> {
        return remember {
            mutableStateOf(
                TextFieldValue(
                    initial,
                    TextRange(initial.length)
                )
            )
        }
    }
}
