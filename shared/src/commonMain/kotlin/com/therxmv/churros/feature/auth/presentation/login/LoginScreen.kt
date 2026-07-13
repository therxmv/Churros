package com.therxmv.churros.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.action_continue_google
import churros.shared.generated.resources.action_create_account
import churros.shared.generated.resources.action_password_hide
import churros.shared.generated.resources.action_password_show
import churros.shared.generated.resources.action_sign_in
import churros.shared.generated.resources.app_tagline
import churros.shared.generated.resources.error_invalid_email
import churros.shared.generated.resources.error_password_too_short
import churros.shared.generated.resources.field_email
import churros.shared.generated.resources.field_password
import churros.shared.generated.resources.login_signing_in
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.components.ChurrosLogo
import com.therxmv.churros.core.design.components.ChurrosPrimaryButton
import com.therxmv.churros.core.design.components.ChurrosSecondaryButton
import com.therxmv.churros.core.design.components.ChurrosTextButton
import com.therxmv.churros.core.design.components.ChurrosTextField
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner, viewModel.effects) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    LoginEffect.NavigateToHome -> onNavigateToHome()
                    LoginEffect.NavigateToRegister -> onNavigateToRegister()
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = ChurrosSpacing.pagePadding)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(ChurrosSpacing.XXL))

            ChurrosLogo()

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))

            Text(
                text = stringResource(Res.string.app_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.XXL))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
            ) {
                ChurrosTextField(
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                    label = stringResource(Res.string.field_email),
                    errorMessage = state.emailError?.let { stringResource(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                ChurrosTextField(
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                    label = stringResource(Res.string.field_password),
                    errorMessage = state.passwordError?.let { stringResource(it) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = stringResource(if (passwordVisible) Res.string.action_password_hide else Res.string.action_password_show),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(ChurrosSpacing.L))

            ChurrosPrimaryButton(
                text = stringResource(if (state.isLoading) Res.string.login_signing_in else Res.string.action_sign_in),
                onClick = { onEvent(LoginEvent.SignInClicked) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))

            ChurrosSecondaryButton(
                text = stringResource(Res.string.action_continue_google),
                onClick = { onEvent(LoginEvent.ContinueWithGoogleClicked) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.M))

            ChurrosTextButton(
                text = stringResource(Res.string.action_create_account),
                onClick = { onEvent(LoginEvent.CreateAccountClicked) },
                enabled = !state.isLoading,
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.L))
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginState(),
        onEvent = {},
    )
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LoginScreenErrorPreview() {
    LoginScreenContent(
        state = LoginState(
            email = "bad",
            password = "123",
            emailError = Res.string.error_invalid_email,
            passwordError = Res.string.error_password_too_short,
        ),
        onEvent = {},
    )
}
