package com.therxmv.churros.feature.auth.presentation.mapper

import churros.shared.generated.resources.Res
import churros.shared.generated.resources.error_auth_apple_not_available
import churros.shared.generated.resources.error_auth_email_already_in_use
import churros.shared.generated.resources.error_auth_expired_link
import churros.shared.generated.resources.error_auth_invalid_credentials
import churros.shared.generated.resources.error_auth_invalid_otp
import churros.shared.generated.resources.error_auth_network
import churros.shared.generated.resources.error_auth_unknown
import churros.shared.generated.resources.error_auth_user_not_found
import churros.shared.generated.resources.error_auth_weak_password
import com.therxmv.churros.feature.auth.domain.model.AuthError
import org.jetbrains.compose.resources.StringResource

/**
 * Maps every [AuthError] variant to its localized [StringResource].
 *
 * Usage in a ViewModel:
 * ```kotlin
 * result.onFailure { error ->
 *     val messageRes = (error as AuthError).toStringResource()
 *     _state.update { it.copy(errorMessage = messageRes) }
 * }
 * ```
 *
 * The `message` field on [AuthError] is an English debug string for logging only —
 * never pass it directly to UI state.
 */
fun AuthError.toStringResource(): StringResource = when (this) {
    is AuthError.InvalidCredentials -> Res.string.error_auth_invalid_credentials
    is AuthError.EmailAlreadyInUse -> Res.string.error_auth_email_already_in_use
    is AuthError.NetworkError -> Res.string.error_auth_network
    is AuthError.InvalidOtp -> Res.string.error_auth_invalid_otp
    is AuthError.ExpiredLink -> Res.string.error_auth_expired_link
    is AuthError.WeakPassword -> Res.string.error_auth_weak_password
    is AuthError.UserNotFound -> Res.string.error_auth_user_not_found
    is AuthError.AppleSignInNotSupported -> Res.string.error_auth_apple_not_available
    is AuthError.Unknown -> Res.string.error_auth_unknown
}
