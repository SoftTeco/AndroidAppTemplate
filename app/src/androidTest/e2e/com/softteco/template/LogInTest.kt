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

class LogInTest {

    private lateinit var driver: AndroidDriver

    @BeforeEach
    fun setUp() {
        val options = BaseOptions()
            .amend("platformName", "Android")
            .amend("appium:automationName", "UiAutomator2")
            .amend("appium:deviceName", "emulator-5554")
            .amend("udid", "emulator-5554")
            .amend("appium:appActivity", "com.softteco.template.MainActivity")
            .amend("appium:appPackage", "com.softteco.template")
            .amend("appium:ensureWebviewsHavePages", true)
            .amend("appium:nativeWebScreenshot", true)
            .amend("appium:connectHardwareKeyboard", true)

        driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)
    }

    @Test
    fun when_credentials_correct_then_log_in_succeeded() {
        driver.run {
            val el1 = findElement(AppiumBy.className("android.widget.Button"))
            el1.click()

            val el2 =
                findElement(AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button"))
            el2.click()

            val el3 =
                findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)")
                )
            el3.click()
            el3.sendKeys("automation@gmail.com")

            val el4 =
                findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(1)")
                )
            el4.click()
            el4.sendKeys("Automation")

            val el5 =
                findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)")
                )
            el5.click()

            manage().timeouts().implicitlyWait(Duration.ofSeconds(3))

            val navBarItem =
                findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Home\")"))

            Truth.assertThat(navBarItem.isDisplayed).isTrue()
        }
    }

    @Test
    fun when_credentials_wrong_then_log_in_failed() {
        driver.run {
            val el1 = findElement(AppiumBy.className("android.widget.Button"))
            el1.click()

            val el2 =
                findElement(AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button"))
            el2.click()

            val el3 = findElement(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)")
            )
            el3.click()
            el3.sendKeys("automation@gmail.com")

            val el4 = findElement(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(1)")
            )
            el4.click()
            el4.sendKeys("Password")

            val el5 = findElement(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)")
            )
            el5.click()

            manage().timeouts().implicitlyWait(Duration.ofSeconds(3))

            val snackbar =
                findElement(
                    AppiumBy.androidUIAutomator(
                        "new UiSelector().text(\"Incorrect email or password. Please check the data and try again.\")"
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
