package com.softteco.template

object Constants {
    const val EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    const val USERNAME_PATTERN = "^[a-zA-Z0-9]{3,}\$"
    const val PASSWORD_PATTERN_CAPITALIZED_LETTER = ".*[A-Z].*"
    const val PASSWORD_PATTERN_MIN = ".{6,}"
    const val CONTACT_EMAIL = "softteco.os.dev@gmail.com"
    const val CONTACT_SUBJECT = "User Inquiry or Feedback"
    const val TERMS_OF_SERVICES_URL = "https://softteco.com/terms-of-services"
    const val REQUEST_RETRY_DELAY = 5000L
}
