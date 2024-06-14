@file:Suppress("MaximumLineLength", "MaxLineLength")

package com.softteco.template

import com.google.common.truth.Truth
import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.options.BaseOptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.Duration

class SignUpTest {

    private lateinit var driver: AndroidDriver

    @BeforeEach
    fun setUp() {
        val options = BaseOptions().amend("platformName", "Android")
            .amend("appium:automationName", "UiAutomator2")
            .amend("appium:deviceName", "emulator-5554").amend("udid", "emulator-5554")
            .amend("appium:appActivity", "com.softteco.template.MainActivity")
            .amend("appium:appPackage", "com.softteco.template")
            .amend("appium:ensureWebviewsHavePages", true).amend("appium:nativeWebScreenshot", true)
            .amend("appium:connectHardwareKeyboard", true)

        driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)
    }

    @Test
    fun when_account_exists_then_sign_up_failed() {
        driver.run {
            val el1 = findElement(AppiumBy.className("android.widget.Button"))
            el1.click()

            val el2 = findElement(
                AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button")
            )
            el2.click()

            val el3 = findElement(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(2)")
            )
            el3.click()

            val el4 = findElement(
                AppiumBy.xpath(
                    "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.widget.EditText[1]"
                )
            )
            el4.click()
            el4.sendKeys("Automation")

            val el5 = findElement(
                AppiumBy.xpath(
                    "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.widget.EditText[2]"
                )
            )
            el5.click()
            el5.sendKeys("automation@gmail.com")

            val el6 = findElement(
                AppiumBy.xpath(
                    "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.widget.EditText[3]"
                )
            )
            el6.click()
            el6.sendKeys("Automation")

            val el7 = findElement(AppiumBy.className("android.widget.CheckBox"))
            el7.click()

            driver.executeScript("mobile: pressKey", mapOf("keycode" to 4))

            val el8 = findElement(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(2)")
            )
            el8.click()

            manage().timeouts().implicitlyWait(Duration.ofSeconds(3))

            val snackbar = findElement(
                AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"Username is already taken. Please choose a different username.\")"
                )
            )

            Truth.assertThat(snackbar.isDisplayed).isTrue()
        }
    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }
}
