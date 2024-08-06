<img src="app/src/main/ic_launcher-playstore.png" alt="icon" width="100"/>

# Template
![test workflow](https://github.com/SoftTeco/AndroidAppTemplate/actions/workflows/test.yml/badge.svg)
[![codecov](https://codecov.io/gh/SoftTeco/AndroidAppTemplate/graph/badge.svg)](https://codecov.io/gh/SoftTeco/AndroidAppTemplate)
![lint workflow](https://github.com/SoftTeco/AndroidAppTemplate/actions/workflows/lint.yml/badge.svg)

# E2E Testing with Appium

## Introduction

**End-to-End (E2E)** testing is a comprehensive testing approach that verifies the functionality of an application from start to finish. E2E tests simulate real user scenarios, ensuring that all integrated components of the application work together as expected. This method provides a high level of confidence in the overall system behavior.

### Why is End-to-End Testing cool?

- **Comprehensive Coverage:** E2E tests cover entire user workflows, ensuring that all parts of the application function together as expected.
- **Integration Validation:** Tests interactions between different components and external interfaces, such as backend services or other apps (e.g., WearOS apps).
- **Real User Scenarios:** Mimics real user interactions, providing a more realistic assessment of application behavior.
- **Early Bug Detection:** Identifies issues that unit or integration tests might miss, particularly those arising from interactions between different parts of the application.
- **Regression Testing:** Facilitates automated regression testing, allowing QA to verify that new code changes do not adversely affect existing functionality. This reduces the need for repetitive manual testing, saving time and effort.

## Appium-Based Automation

Appium is a popular open-source tool for automating mobile and web applications. It enables robust E2E testing by automating user interface interactions.

### Key Components

1. **Appium Server:** Acts as a bridge between your test code and the mobile device/emulator. It receives commands from the test code and executes them on the device.
2. **Appium Client:** The library used in your test scripts to send commands to the Appium server. It is available for various languages, including Java and Kotlin.
3. **Emulator or Real Device:** Tests can be run on either an emulator or a physical device. Each test session requires a dedicated instance of the Appium server.

### Getting Started with Appium

To get started with Appium, refer to the [Appium Quickstart Guide](https://appium.io/docs/en/latest/quickstart).

### Test Creation and Execution

1. **Recording Actions with Appium Inspector:**
    - Appium Inspector helps identify UI elements on the device screen and record user interactions.
    - You can generate test scripts in various languages and frameworks, such as JUnit4/5 for Java or Kotlin.

2. **Running Tests:**
    - Ensure the Appium server is running and the device/emulator is connected.
    - Execute the tests from your IDE or command line.

## Implementation Details

This repository includes several example E2E tests for the **Template** project. These tests demonstrate how to interact with UI elements using **UiAutomator** and **XPath**.

### Example Tests

- **UiAutomator Example:**

  ```kotlin
  val el3 = findElement(
      AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)")
  )
  ```
  
- **XPath Example:**

  ```kotlin
  val el4 = findElement(
      AppiumBy.xpath(
          "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.widget.EditText[1]"
      )
  )
  ```
  
### Flaky Tests and Delays

E2E tests can sometimes be flaky due to timing issues or network delays. It's essential to:

- Introduce appropriate waits or retries to handle asynchronous operations.
- Use explicit waits to wait for elements to be available before interacting with them.

  **Example of a delay for executing a network request:**

  ```kotlin
  manage().timeouts().implicitlyWait(Duration.ofSeconds(3))
  ```

## Running E2E Tests

To facilitate running E2E tests alongside unit and integration tests, separate Android Studio run configurations have been created. These configurations ensure that tests can be executed individually or sequentially.

### Run Configurations

- **Unit**: Runs all unit tests.
- **Integration**: Runs all integration tests.
- **E2E**: Runs all E2E tests using Appium.
- **AllTests**: Combines all test types into a single run configuration.

### Executing All Tests

To run all tests sequentially:

1. Open Android Studio.
2. Select the **AllTests** run configuration.
3. Click the run button to execute unit, integration, and E2E tests in sequence.