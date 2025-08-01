package site.dogether.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import site.dogether.common.exception.NetworkErrorException
import site.dogether.common.exception.NetworkFailureException
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.ApiRoutes
import site.dogether.domain.model.DomainModel

fun createUrl(apiRoute: String): String = "${ApiRoutes.BASE_URL}/$apiRoute"

fun <Res : DataModel, Domain : DomainModel> Result<Res>.mapToDomain(
    mapper: DataMapper<Res, Domain>,
): Result<Domain> {
    return mapCatching { res -> mapper.run { res.toDomainModel() } }
}

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse,
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

suspend inline fun <reified Req, reified Res : DataModel, Domain : DomainModel> HttpClient.safePost(
    apiRoute: String,
    body: Req,
    mapper: DataMapper<Res, Domain>,
): Result<Domain> {
    return safeApiCall<Res> {
        post(createUrl(apiRoute)) {
            setBody(body)
        }
    }.mapToDomain(mapper)
}

suspend inline fun <reified Res : DataModel, Domain : DomainModel> HttpClient.safeGet(
    apiRoute: String,
    params: Map<String, String> = emptyMap(),
    mapper: DataMapper<Res, Domain>,
): Result<Domain> {
    return safeApiCall<Res> {
        get(createUrl(apiRoute)) {
            params.forEach { (key, value) -> parameter(key, value) }
        }
    }.mapToDomain(mapper)
}