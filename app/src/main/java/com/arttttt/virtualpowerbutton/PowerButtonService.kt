package com.arttttt.virtualpowerbutton

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class PowerButtonService : AccessibilityService() {

    companion object {
        var instance: PowerButtonService? = null
            private set
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    fun lockScreen() {
        performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
    }

    fun showPowerDialog() {
        performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}