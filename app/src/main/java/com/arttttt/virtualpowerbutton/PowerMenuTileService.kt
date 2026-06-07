package com.arttttt.virtualpowerbutton

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.core.service.quicksettings.PendingIntentActivityWrapper
import androidx.core.service.quicksettings.TileServiceCompat
import com.arttttt.virtualpowerbutton.utils.VibrationManager
import com.arttttt.virtualpowerbutton.utils.mainActivityIntent

class PowerMenuTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()

        val tile = qsTile ?: return

        tile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_power)
        tile.label = getString(R.string.power_button)
        tile.state = Tile.STATE_INACTIVE
        tile.subtitle = when {
            PowerButtonService.isRunning -> null
            else -> getString(R.string.tile_setup_required)
        }
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (PowerButtonService.isRunning) {
            launchAndCollapse(Intent(this, PowerMenuActivity::class.java))
            VibrationManager.vibrate(VibrationManager.Effect.Click)
        } else {
            launchAndCollapse(mainActivityIntent(this))
        }
    }

    private fun launchAndCollapse(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val wrapper = PendingIntentActivityWrapper(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT,
            false,
        )

        TileServiceCompat.startActivityAndCollapse(this, wrapper)
    }
}
