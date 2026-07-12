package com.therxmv.churros.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.churros.feature.auth.domain.usecase.ValidateEmailUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ValidateNameUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val validateName: ValidateNameUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effects = Channel<RegisterEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> _state.update { it.copy(name = event.name, nameError = null) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(email = event.email, emailError = null) }
            is RegisterEvent.PasswordChanged -> _state.update { it.copy(password = event.password, passwordError = null) }
            RegisterEvent.SignUpClicked -> handleSignUp()
            RegisterEvent.ContinueWithGoogleClicked -> mockRegister()
            RegisterEvent.SignInClicked -> navigateToLogin()
        }
    }

    private fun handleSignUp() {
        val current = _state.value
        val nameValid = validateName(current.name)
        val emailValid = validateEmail(current.email)
        val passwordValid = validatePassword(current.password)

        if (!nameValid || !emailValid || !passwordValid) {
            _state.update {
                it.copy(
                    nameError = if (!nameValid) "Name is required" else null,
                    emailError = if (!emailValid) "Enter a valid email address" else null,
                    passwordError = if (!passwordValid) "Password must be at least 8 characters" else null,
                )
            }
            return
        }

        mockRegister()
    }

    private fun mockRegister() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _effects.send(RegisterEffect.NavigateToHome)
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            _effects.send(RegisterEffect.NavigateToLogin)
        }
    }
}
