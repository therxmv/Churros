package com.therxmv.churros.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.error_invalid_email
import churros.shared.generated.resources.error_password_too_short
import com.therxmv.churros.feature.auth.domain.repository.SessionRepository
import com.therxmv.churros.feature.auth.domain.usecase.ValidateEmailUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sessionRepository: SessionRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is LoginEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            LoginEvent.SignInClicked -> handleSignIn()
            LoginEvent.ContinueWithGoogleClicked -> mockLogin()
            LoginEvent.CreateAccountClicked -> navigateToRegister()
        }
    }

    private fun handleSignIn() {
        val current = _state.value
        val emailValid = validateEmail(current.email)
        val passwordValid = validatePassword(current.password)

        if (!emailValid || !passwordValid) {
            _state.update {
                it.copy(
                    emailError = if (!emailValid) Res.string.error_invalid_email else null,
                    passwordError = if (!passwordValid) Res.string.error_password_too_short else null,
                )
            }
            return
        }

        mockLogin()
    }

    private fun mockLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            sessionRepository.saveSession()
            _effects.send(LoginEffect.NavigateToHome)
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch {
            _effects.send(LoginEffect.NavigateToRegister)
        }
    }
}
