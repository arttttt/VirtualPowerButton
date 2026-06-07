package com.arttttt.virtualpowerbutton

import androidx.lifecycle.ViewModel
import com.arttttt.virtualpowerbutton.utils.AccessibilityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val accessibilityManager: AccessibilityManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainUiState(
            isAccessibilityEnabled = false,
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        updateState()
    }

    fun requestAccessibilityPermission() {
        accessibilityManager.requestAccessibilityPermission()
    }

    fun updateState() {
        _uiState.update {
            MainUiState(
                isAccessibilityEnabled = accessibilityManager.isAccessibilityEnabled,
            )
        }
    }
}
