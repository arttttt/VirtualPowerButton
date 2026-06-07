package com.arttttt.virtualpowerbutton

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.arttttt.virtualpowerbutton.utils.mainActivityIntent

class PowerButtonActivity : ComponentActivity() {

    companion object {
        const val ACTION_LOCK_SCREEN = "com.arttttt.virtualpowerbutton.LOCK_SCREEN"
        const val ACTION_POWER_MENU = "com.arttttt.virtualpowerbutton.POWER_MENU"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handled = when (intent.action) {
            ACTION_POWER_MENU -> PowerButtonService.instance?.showPowerDialog()
            // Default action (incl. launcher icon / shortcut) locks the screen.
            else -> PowerButtonService.instance?.lockScreen()
        }

        if (handled == null) {
            onServiceUnavailable()
        }

        finish()
    }

    private fun onServiceUnavailable() {
        Toast.makeText(
            this,
            R.string.accessibility_permission_required,
            Toast.LENGTH_LONG
        ).show()

        startActivity(mainActivityIntent(this))
    }
}
