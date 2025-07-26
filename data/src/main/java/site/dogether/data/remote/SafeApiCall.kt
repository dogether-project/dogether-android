package site.dogether.data.remote

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import site.dogether.common.exception.NetworkErrorException
import site.dogether.common.exception.NetworkFailureException

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse
): Result<T> {
    return try {
        val response = apiCall()
        if (response.status.isSuccess()) {
            val body = response.body<T>()
            Result.success(body)
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(NetworkFailureException(response.status.value, errorBody))
        }
    } catch (e: Throwable) {
        Result.failure(NetworkErrorException(e))
    }
}