package site.dogether.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import site.dogether.common.exception.NetworkFailureException
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.BaseResponse
import site.dogether.domain.model.DomainModel

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

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
            val text = response.bodyAsText()
            val baseResponse = json.decodeFromString<BaseResponse<T>>(text)
            val data = baseResponse.data
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(IllegalStateException("Response body 'data' is null"))
            }
        } else {
            val parsedErrorBody = json.decodeFromString<BaseResponse<Nothing>>(response.bodyAsText())
            Result.failure(
                NetworkFailureException(
                    code = parsedErrorBody.code,
                    message = parsedErrorBody.message
                )
            )
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

suspend inline fun <reified Req, reified Res : DataModel, Domain : DomainModel> HttpClient.safePost(
    apiRoute: String,
    body: Req,
    mapper: DataMapper<Res, Domain>,
): Result<Domain> {
    return safeApiCall<Res> {
        post(apiRoute) {
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
        get(apiRoute) {
            params.forEach { (key, value) -> parameter(key, value) }
        }
    }.mapToDomain(mapper)
}