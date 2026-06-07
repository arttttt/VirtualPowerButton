package com.arttttt.virtualpowerbutton.utils

import android.content.Context
import android.content.Intent
import com.arttttt.virtualpowerbutton.MainActivity

/**
 * Intent that brings the main screen to the front, used as a fallback when the
 * accessibility service is not available yet and the user has to be guided to the setup.
 */
fun mainActivityIntent(context: Context): Intent {
    return Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
}
