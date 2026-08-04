package com.therxmv.churros.feature.family.domain.model

/**
 * Domain-layer errors raised by family repository operations.
 *
 * Mirrors the structure of [com.therxmv.churros.feature.auth.domain.model.AuthError]
 * and [com.therxmv.churros.feature.chores.domain.model.ChoreError] so the presentation
 * layer can handle errors consistently across features.
 */
sealed class FamilyError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** The authenticated user is not a member of any household. */
    data object HouseholdNotFound : FamilyError(
        // TODO(Phase 3 — Localization): localize error message
        message = "No household found — please create or join a household first",
    )

    /** The target member does not exist or is not in the current household. */
    data object MemberNotFound : FamilyError(
        // TODO(Phase 3 — Localization): localize error message
        message = "Member not found",
    )

    /** The operation is forbidden by the database's Row Level Security policies. */
    data object Unauthorized : FamilyError(
        // TODO(Phase 3 — Localization): localize error message
        message = "You do not have permission to perform this action",
    )

    /** The device has no network connection or the Supabase host is unreachable. */
    data object NetworkError : FamilyError(
        // TODO(Phase 3 — Localization): localize error message
        message = "Network error — please check your connection",
    )

    /** Catch-all for unexpected failures. */
    data class Unknown(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : FamilyError(message = message, cause = cause)
}
