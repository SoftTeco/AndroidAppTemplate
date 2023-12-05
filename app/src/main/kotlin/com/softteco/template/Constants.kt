package com.softteco.template

object Constants {
    const val EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    const val PASSWORD_PATTERN_CAPITALIZED_LETTER = ".*[A-Z].*"
    const val PASSWORD_PATTERN_MIN = ".{6,}"
    const val SPACE_STRING = " "
    const val CONTACT_EMAIL = "softteco.os.dev@gmail.com"
    const val CONTACT_SUBJECT = "User Inquiry or Feedback"
    const val ALPHA_COLOR_VALUE = 255
    const val BOUND_COLOR_VALUE = 256
    const val CHARTS_STEP_VALUE = 1.0F
    const val START_INDEX_OF_TEMPERATURE = 0
    const val END_INDEX_OF_TEMPERATURE = 2
    const val INDEX_OF_HUMIDITY = 2
    const val START_INDEX_OF_BATTERY = 3
    const val END_INDEX_OF_BATTERY = 5
    const val DIVISION_VALUE_OF_VALUES = 100
    const val BIT_SHIFT_VALUE = 8
}
