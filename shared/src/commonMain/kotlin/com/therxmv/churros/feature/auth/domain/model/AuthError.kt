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

    /** The 6-digit OTP is incorrect or has already been used. */
    data object InvalidOtp : AuthError(message = "The verification code is incorrect")

    /** The password-reset or verify link has expired. */
    data object ExpiredLink : AuthError(message = "The link has expired — please request a new one")

    /** The new password does not meet the minimum strength requirements. */
    data object WeakPassword : AuthError(message = "Password must be at least 8 characters, include a number and an uppercase letter")

    /** Catch-all for unexpected failures. */
    data class Unknown(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : AuthError(message = message, cause = cause)
}
