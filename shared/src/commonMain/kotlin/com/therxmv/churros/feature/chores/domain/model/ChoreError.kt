package com.therxmv.churros.feature.chores.domain.model

/**
 * Domain-layer errors raised by chore repository operations.
 *
 * Mirrors the structure of `AuthError` so the presentation layer can handle
 * errors consistently across features.
 */
sealed class ChoreError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** The authenticated user is not a member of any household. */
    data object HouseholdNotFound : ChoreError(
        // TODO(Phase 3 — Localization): localize error message
        message = "No household found — please create or join a household first",
    )

    /** The requested chore does not exist or is not visible to the current user. */
    // TODO(Phase 3 — Localization): localize error message
    data object NotFound : ChoreError(message = "Chore not found")

    /** The operation is forbidden by the database's Row Level Security policies. */
    data object Unauthorized : ChoreError(
        // TODO(Phase 3 — Localization): localize error message
        message = "You do not have permission to perform this action",
    )

    /** The device has no network connection or the Supabase host is unreachable. */
    data object NetworkError : ChoreError(
        // TODO(Phase 3 — Localization): localize error message
        message = "Network error — please check your connection",
    )

    /** Catch-all for unexpected failures. */
    data class Unknown(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : ChoreError(message = message, cause = cause)
}
