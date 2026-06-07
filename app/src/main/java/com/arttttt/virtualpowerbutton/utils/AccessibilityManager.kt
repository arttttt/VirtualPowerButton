package com.arttttt.virtualpowerbutton.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import com.arttttt.virtualpowerbutton.PowerButtonService

class AccessibilityManager(
    private val context: Context
) {
    val isAccessibilityEnabled: Boolean
        get() {
            val expected = ComponentName(context, PowerButtonService::class.java)

            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            val splitter = TextUtils.SimpleStringSplitter(':')
            splitter.setString(enabledServices)

            return splitter.any { component ->
                ComponentName.unflattenFromString(component) == expected
            }
        }

    fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
