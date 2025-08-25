package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.jetbrains.annotations.VisibleForTesting
import site.dogether.data.local.DataStoreManager
import site.dogether.data.local.PreferenceKey
import site.dogether.data.model.JwtPayload
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.user.KakaoLoginReq
import site.dogether.data.remote.model.res.user.CheckParticipatingResMapper
import site.dogether.data.remote.model.res.user.UserInfoRes
import site.dogether.data.remote.model.res.user.UserInfoResMapper
import site.dogether.data.utils.safeGet
import site.dogether.data.utils.safePost
import site.dogether.domain.model.user.ParticipatingInfo
import site.dogether.domain.model.user.UserInfo
import site.dogether.domain.repository.UserRepository
import kotlin.io.encoding.Base64

class UserRepositoryImpl(
    private val dataStoreManager: DataStoreManager,
    private val httpClient: HttpClient,
) : UserRepository {

    override suspend fun storeUserInfo(
        name: String,
        accessToken: String
    ): Result<Unit> {
        return dataStoreManager.storeString(
            key = PreferenceKey.USER_NAME,
            data = name
        ).fold(
            onSuccess = {
                dataStoreManager.storeString(
                    key = PreferenceKey.USER_TOKEN,
                    data = accessToken
                )
            },
            onFailure = { e ->
                Result.failure(e)
            }
        )
    }

    override suspend fun getUserInfo(): Result<UserInfo> {
        return dataStoreManager.loadString(PreferenceKey.USER_NAME).fold(
            onSuccess = { name ->
                val accessToken = dataStoreManager.loadString(PreferenceKey.USER_TOKEN).getOrElse { e ->
                    return Result.failure(e)
                }

                Result.success(
                    UserInfo(
                        name = name,
                        accessToken = accessToken
                    )
                )
            },
            onFailure = { e ->
                Result.failure(e)
            }
        )
    }

    override suspend fun checkParticipating(): Result<ParticipatingInfo> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.CHECK_PARTICIPATING,
            mapper = CheckParticipatingResMapper
        )
    }

    override suspend fun loginWithKakao(
        name: String,
        idToken: String,
    ): Result<UserInfo> {
        return httpClient.safePost<KakaoLoginReq, UserInfoRes, UserInfo>(
            apiRoute = ApiRoutes.KAKAO_LOGIN,
            body = KakaoLoginReq(
                loginType = "KAKAO",
                name = name,
                providerId = extractUserIdFromJwt(idToken)
            ),
            mapper = UserInfoResMapper
        )
    }

    @VisibleForTesting
    internal fun extractUserIdFromJwt(idToken: String): String {
        val parts = idToken.split(".")
        var payloadBase64 = parts[1]
            .replace('-', '+')
            .replace('_', '/')
        val pad = (4 - payloadBase64.length % 4) % 4
        payloadBase64 += "=".repeat(pad)

        val payloadJson = Base64.decode(payloadBase64).decodeToString()

        val json = Json { ignoreUnknownKeys = true }
        val payload = json.decodeFromString<JwtPayload>(payloadJson)

        return payload.sub
    }
}