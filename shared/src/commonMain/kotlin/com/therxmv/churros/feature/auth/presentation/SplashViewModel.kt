package com.therxmv.churros.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.churros.feature.auth.domain.repository.SessionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _effects = Channel<SplashEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            if (sessionRepository.isLoggedIn()) {
                _effects.send(SplashEffect.NavigateToHome)
            } else {
                _isLoading.value = false
            }
        }
    }
}
