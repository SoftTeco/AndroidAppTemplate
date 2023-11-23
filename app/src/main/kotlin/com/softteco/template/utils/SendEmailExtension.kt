package com.softteco.template.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.sendMail(recipient: String, subject: String) {
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(this)
    }
}
