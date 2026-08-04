package com.therxmv.churros.feature.settings.domain.model

/**
 * Domain-layer errors raised by settings / user-profile repository operations.
 *
 * Mirrors the structure of [com.therxmv.churros.feature.auth.domain.model.AuthError] so
 * the presentation layer can handle errors consistently across features.
 */
sealed class SettingsError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** No active session — the user must be signed in to perform this action. */
    data object Unauthorized : SettingsError(
        // TODO(Phase 3 — Localization): localize error message
        message = "You must be signed in to perform this action",
    )

    /** The `public.profiles` row for the current user could not be found. */
    data object ProfileNotFound : SettingsError(
        // TODO(Phase 3 — Localization): localize error message
        message = "User profile not found",
    )

    /** The device has no network connection or the Supabase host is unreachable. */
    data object NetworkError : SettingsError(
        // TODO(Phase 3 — Localization): localize error message
        message = "Network error — please check your connection",
    )

    /** Catch-all for unexpected failures. */
    data class Unknown(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : SettingsError(message = message, cause = cause)
}
