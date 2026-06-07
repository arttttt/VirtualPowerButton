package com.arttttt.virtualpowerbutton

import androidx.lifecycle.ViewModel
import com.arttttt.virtualpowerbutton.utils.AccessibilityManager
import com.arttttt.virtualpowerbutton.utils.ShortcutManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val accessibilityManager: AccessibilityManager,
    private val shortcutManager: ShortcutManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainUiState(
            isAccessibilityEnabled = false,
            isShortcutCreated = false,
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        updateState()
    }

    fun requestAccessibilityPermission() {
        accessibilityManager.requestAccessibilityPermission()
    }

    fun toggleShortcut() {
        if (!accessibilityManager.isAccessibilityEnabled) return

        if (shortcutManager.isShortcutCreated()) {
            shortcutManager.resetShortcutState()
        } else {
            shortcutManager.createShortcut()
        }

        updateState()
    }

    fun updateState() {
        _uiState.update {
            MainUiState(
                isAccessibilityEnabled = accessibilityManager.isAccessibilityEnabled,
                isShortcutCreated = shortcutManager.isShortcutCreated()
            )
        }
    }
}