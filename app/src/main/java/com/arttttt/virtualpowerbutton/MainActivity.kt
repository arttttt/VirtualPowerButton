package com.arttttt.virtualpowerbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arttttt.virtualpowerbutton.ui.theme.VirtualPowerButtonTheme
import com.arttttt.virtualpowerbutton.utils.AccessibilityManager
import com.arttttt.virtualpowerbutton.utils.ShortcutManager

class MainActivity : ComponentActivity() {

    private val accessibilityManager by lazy { AccessibilityManager(this) }
    private val shortcutHelper by lazy { ShortcutManager(this) }
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(accessibilityManager, shortcutHelper) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VirtualPowerButtonTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                MainScreen(
                    uiState = uiState,
                    onRequestAccessibility = viewModel::requestAccessibilityPermission,
                    onToggleShortcut = viewModel::toggleShortcut
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateState()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(
        modifier: Modifier = Modifier,
        uiState: MainUiState,
        onRequestAccessibility: () -> Unit,
        onToggleShortcut: () -> Unit,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                        )
                    },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!uiState.isAccessibilityEnabled) {
                    AccessibilityPermissionRequest(
                        onRequestPermission = onRequestAccessibility
                    )
                } else {
                    ShortcutSection(
                        isShortcutCreated = uiState.isShortcutCreated,
                        onToggleShortcut = onToggleShortcut
                    )
                }
            }
        }
    }

    @Composable
    private fun AccessibilityPermissionRequest(
        onRequestPermission: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.accessibility_permission_explanation),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 16.dp,
                )
            )

            ElevatedButton(
                onClick = onRequestPermission,
                modifier = Modifier.widthIn(min = 200.dp),
            ) {
                Text(text = stringResource(R.string.request_accessibility))
            }
        }
    }

    @Composable
    private fun ShortcutSection(
        isShortcutCreated: Boolean,
        onToggleShortcut: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledTonalButton(
                onClick = onToggleShortcut,
                modifier = Modifier.widthIn(min = 200.dp),
            ) {
                Text(
                    text = stringResource(
                        if (isShortcutCreated) R.string.shortcut_remove else R.string.shortcut_add
                    )
                )
            }

            Text(
                text = stringResource(R.string.shortcut_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}