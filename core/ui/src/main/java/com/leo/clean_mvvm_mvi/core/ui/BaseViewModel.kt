package com.leo.clean_mvvm_mvi.core.ui

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Action : UiAction, Event : UiEvent>(
    protected val savedStateHandle: SavedStateHandle,
    initialState: State,
    private val stateKey: String = DEFAULT_STATE_KEY
) : ViewModel() {

    companion object {
        const val DEFAULT_STATE_KEY = "saved_ui_state_key"
    }

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(
        savedStateHandle.get<Bundle>(stateKey)?.let { restoreState(it) } ?: initialState
    )
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEvent = Channel<Event>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    val currentState: State
        get() = _uiState.value

    abstract fun onAction(action: Action)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.update { current ->
            val newState = current.reducer()
            persistState(newState)
            newState
        }
    }

    private fun persistState(state: State) {
        val bundle = saveState(state)
        if (bundle != null) {
            savedStateHandle[stateKey] = bundle
        }
    }

    protected open fun saveState(state: State): Bundle? = null
    protected open fun restoreState(bundle: Bundle): State? = null

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
