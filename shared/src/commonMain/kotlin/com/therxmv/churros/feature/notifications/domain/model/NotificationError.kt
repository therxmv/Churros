package com.therxmv.churros.feature.notifications.domain.model

/**
 * Domain error types for the Notifications feature.
 */
sealed class NotificationError : Exception() {

    /** The user is not authenticated or the session has expired. */
    data object Unauthorized : NotificationError()

    /** A transient network failure occurred. */
    data object NetworkError : NotificationError()

    /** An unexpected error occurred. */
    data class Unknown(
        override val message: String?,
        override val cause: Throwable? = null,
    ) : NotificationError()
}
