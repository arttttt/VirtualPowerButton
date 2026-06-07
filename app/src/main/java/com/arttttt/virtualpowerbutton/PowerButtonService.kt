package com.arttttt.virtualpowerbutton

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.arttttt.virtualpowerbutton.utils.VibrationManager

class PowerButtonService : AccessibilityService() {

    companion object {
        var instance: PowerButtonService? = null
            private set

        /**
         * Single source of truth for whether the service is connected and able to
         * perform global actions right now.
         */
        val isRunning: Boolean
            get() = instance != null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    /**
     * @return true if the system accepted the lock-screen action.
     */
    fun lockScreen(): Boolean {
        VibrationManager.vibrate(VibrationManager.Effect.LockScreen)

        return performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
    }

    /**
     * @return true if the system accepted the power-dialog action.
     */
    fun showPowerDialog(): Boolean {
        VibrationManager.vibrate(VibrationManager.Effect.Click)

        return performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
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
