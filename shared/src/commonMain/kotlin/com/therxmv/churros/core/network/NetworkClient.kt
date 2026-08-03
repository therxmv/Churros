package com.therxmv.churros.core.network

import co.touchlab.kermit.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val kermitLogger = Logger.withTag("NetworkClient")

fun createHttpClient(
    engine: HttpClientEngine,
    supabaseClient: SupabaseClient,
): HttpClient = HttpClient(engine) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            },
        )
    }
    install(Logging) {
        logger = object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                kermitLogger.d { message }
            }
        }
        level = LogLevel.BODY
    }
    defaultRequest {
        val token = supabaseClient.auth.currentAccessTokenOrNull()
        if (token != null) {
            headers.append("Authorization", "Bearer $token")
        }
    }
    HttpResponseValidator {
        validateResponse { response ->
            val statusCode = response.status.value
            when (statusCode) {
                in 400..499 -> throw NetworkError.ClientError(
                    statusCode = statusCode,
                    message = response.status.description,
                )
                in 500..599 -> throw NetworkError.ServerError(
                    statusCode = statusCode,
                    message = response.status.description,
                )
            }
        }
        handleResponseExceptionWithRequest { exception, _ ->
            if (exception is NetworkError) throw exception
            throw NetworkError.UnknownError(
                cause = exception,
                message = exception.message,
            )
        }
    }
}
