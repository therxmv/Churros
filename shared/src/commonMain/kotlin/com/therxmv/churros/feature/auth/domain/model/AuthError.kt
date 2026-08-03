package com.therxmv.churros.feature.auth.domain.model

sealed class AuthError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** Email or password is incorrect. */
    data object InvalidCredentials : AuthError(message = "Invalid email or password")

    /** An account with this email address already exists. */
    data object EmailAlreadyInUse : AuthError(message = "An account with this email already exists")

    /** The device has no network connection or the Supabase host is unreachable. */
    data object NetworkError : AuthError(message = "Network error — please check your connection")

    /** Catch-all for unexpected failures. */
    data class Unknown(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : AuthError(message = message, cause = cause)
}
