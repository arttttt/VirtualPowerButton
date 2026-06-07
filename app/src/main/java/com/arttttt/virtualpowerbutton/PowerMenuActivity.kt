package com.arttttt.virtualpowerbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arttttt.virtualpowerbutton.ui.theme.VirtualPowerButtonTheme

class PowerMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VirtualPowerButtonTheme {
                PowerMenuSheet(
                    onDismiss = ::finish,
                    onLockScreen = {
                        PowerButtonService.instance?.lockScreen()
                        finish()
                    },
                    onPowerMenu = {
                        PowerButtonService.instance?.showPowerDialog()
                        finish()
                    }
                )
            }
        }
    }

    @Composable
    private fun PowerMenuSheet(
        onDismiss: () -> Unit,
        onLockScreen: () -> Unit,
        onPowerMenu: () -> Unit,
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MenuItem(
                        text = stringResource(R.string.lock_screen),
                        onClick = onLockScreen,
                    )

                    MenuItem(
                        text = stringResource(R.string.power_menu),
                        onClick = onPowerMenu,
                    )
                }
            }
        }
    }

    @Composable
    private fun MenuItem(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
