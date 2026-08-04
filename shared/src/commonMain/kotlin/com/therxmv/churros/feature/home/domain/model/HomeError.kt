package com.therxmv.churros.feature.home.domain.model

/**
 * Domain error types for the Home feature.
 */
sealed class HomeError : Exception() {

    /** The user is not authenticated. */
    data object Unauthorized : HomeError()

    /** The user is not a member of any household. */
    data object HouseholdNotFound : HomeError()

    /** A transient network failure occurred. */
    data object NetworkError : HomeError()

    /** An unexpected error occurred. */
    data class Unknown(
        override val message: String?,
        override val cause: Throwable? = null,
    ) : HomeError()
}
