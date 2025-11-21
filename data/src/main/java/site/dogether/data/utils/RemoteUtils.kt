package site.dogether.data.utils

import android.content.Context
import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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
            val parsedErrorBody =
                json.decodeFromString<BaseResponse<Nothing>>(response.bodyAsText())
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

suspend inline fun safeApiCallWithoutRes(
    apiCall: suspend () -> HttpResponse,
): Result<Unit> {
    return try {
        val response = apiCall()
        if (response.status.isSuccess()) {
            Result.success(Unit)
        } else {
            val parsedErrorBody =
                json.decodeFromString<BaseResponse<Nothing>>(response.bodyAsText())
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
    params: Map<String, Any> = emptyMap(),
    mapper: DataMapper<Res, Domain>,
): Result<Domain> {
    return safeApiCall<Res> {
        get(apiRoute) {
            params.forEach { (key, value) -> parameter(key, value) }
        }
    }.mapToDomain(mapper)
}

suspend inline fun <reified Req> HttpClient.safePostWithoutRes(
    apiRoute: String,
    body: Req,
): Result<Unit> {
    return safeApiCallWithoutRes {
        post(apiRoute) {
            setBody(body)
        }
    }
}

suspend fun HttpClient.safeGetWithoutRes(
    apiRoute: String,
    params: Map<String, String> = emptyMap(),
): Result<Unit> {
    return safeApiCallWithoutRes {
        get(apiRoute) {
            params.forEach { (key, value) -> parameter(key, value) }
        }
    }
}

suspend inline fun <reified Req> HttpClient.safeDeleteWithoutRes(
    apiRoute: String,
    body: Req,
): Result<Unit> {
    return safeApiCallWithoutRes {
        delete(apiRoute) {
            setBody(body)
        }
    }
}

/**
 * S3 업로드를 위한 PUT 요청 (Presigned URL 사용)
 * Content-Type 헤더만 사용 (Presigned URL의 SignedHeaders에 맞춤)
 * @param presignedUrl S3 Presigned URL
 * @param imageUri 업로드할 이미지 URI
 * @param context Context (Uri를 ByteArray로 변환하기 위해 필요)
 * @return 업로드 성공 여부
 */
suspend fun safePutToS3(
    presignedUrl: String,
    imageUri: Uri,
    context: Context,
): Result<Unit> {
    return try {
        // 1. 이미지 데이터 읽기
        val imageBytes = imageUri.toByteArray(context)

        if (imageBytes.isEmpty()) {
            return Result.failure(IllegalArgumentException("Image is empty"))
        }

        // Presigned URL에서 실제 서명된 Content-Type 추출
        val contentType = try {
            // Presigned URL의 query parameters에서 content-type 추출
            val queryParams = presignedUrl.split("?").getOrNull(1) ?: ""
            val signedHeadersParam =
                queryParams.split("&").find { it.startsWith("X-Amz-SignedHeaders=") }

            if (signedHeadersParam != null) {
                val signedHeaders = signedHeadersParam.substringAfter("X-Amz-SignedHeaders=")

                // content-type이 서명에 포함되어 있는지 확인
                if (signedHeaders.contains("content-type")) {
                    // 서버에서 Presigned URL 생성 시 사용한 Content-Type을 추정
                    // 일반적으로 서버에서는 파일 확장자에 따라 Content-Type을 설정
                    val urlExtension =
                        presignedUrl.substringAfterLast('.', "").substringBefore('?').lowercase()

                    when (urlExtension) {
                        "png" -> "image/png"
                        "jpg", "jpeg" -> "image/jpeg"
                        "webp" -> "image/webp"
                        "gif" -> "image/gif"
                        else -> {
                            // URL 확장자가 없거나 알 수 없는 경우, 실제 파일 확장자 확인
                            val fileExtension =
                                imageUri.path?.substringAfterLast('.', "")?.lowercase()
                            when (fileExtension) {
                                "png" -> "image/png"
                                "jpg", "jpeg" -> "image/jpeg"
                                "webp" -> "image/webp"
                                "gif" -> "image/gif"
                                else -> "image/jpeg" // 기본값
                            }
                        }
                    }
                } else {
                    // content-type이 서명에 포함되지 않은 경우
                    "image/jpeg" // 기본값
                }
            } else {
                "image/jpeg" // 기본값
            }
        } catch (e: Exception) {
            "image/jpeg" // 기본값
        }

        // 2. HttpClient 생성 (Content-Type 자동 추가 완전 차단)
        val client = HttpClient(CIO) {
            engine {
                requestTimeout = 60000L
            }
            // Content-Type 자동 추가 방지
            expectSuccess = false
        }

        // 3. Presigned URL 파싱하여 정확한 헤더 설정
        val possibleContentTypes = listOf(
            contentType, // 첫 번째 시도: 추정된 Content-Type
            "image/png", // 두 번째 시도: URL 확장자 기반
            "image/jpeg", // 세 번째 시도: 실제 파일 타입
            "image/*" // 네 번째 시도: 와일드카드
        ).distinct()

        var lastError: Exception? = null

        for (attemptContentType in possibleContentTypes) {
            val response = client.put(presignedUrl) {
                setBody(imageBytes)
                // Ktor의 자동 Content-Type 추가를 강제로 차단
                headers.remove("Content-Type")
                // Presigned URL의 서명과 일치하는 Content-Type 설정
                headers.set("Content-Type", attemptContentType)
            }

            if (response.status.isSuccess()) {
                client.close()
                return Result.success(Unit)
            } else {
                val errorBody = try {
                    response.bodyAsText()
                } catch (e: Exception) {
                    "Cannot read error body: ${e.message}"
                }

                lastError =
                    Exception("S3 upload failed with Content-Type $attemptContentType: ${response.status}\nError: $errorBody")

                // SignatureDoesNotMatch가 아닌 다른 에러인 경우 즉시 중단
                if (!errorBody.contains("SignatureDoesNotMatch")) {
                    break
                }
            }
        }

        client.close()
        return Result.failure(lastError ?: Exception("All Content-Type attempts failed"))

    } catch (e: Throwable) {
        Result.failure(e)
    }
}