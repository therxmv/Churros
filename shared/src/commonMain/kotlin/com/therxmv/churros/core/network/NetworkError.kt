package com.therxmv.churros.core.network

sealed class NetworkError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    data class ClientError(
        val statusCode: Int,
        override val message: String? = null,
    ) : NetworkError(message = message)

    data class ServerError(
        val statusCode: Int,
        override val message: String? = null,
    ) : NetworkError(message = message)

    data class UnknownError(
        override val cause: Throwable? = null,
        override val message: String? = null,
    ) : NetworkError(message = message, cause = cause)
}
