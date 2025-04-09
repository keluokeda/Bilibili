package com.ke.biliblli.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State, Action, Event>(initialState: State) : ViewModel() {


    protected val _uiState = MutableStateFlow(initialState)


    val uiState = _uiState.asStateFlow()

    protected val _event = Channel<Event>(capacity = Channel.CONFLATED)

    val event = _event.receiveAsFlow()

    abstract fun handleAction(action: Action)
}