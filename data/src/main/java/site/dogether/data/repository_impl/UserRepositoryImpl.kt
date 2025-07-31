package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.user.KakaoLoginReq
import site.dogether.data.remote.model.res.user.UserInfoRes
import site.dogether.data.remote.model.res.user.UserInfoResMapper
import site.dogether.data.utils.safePost
import site.dogether.domain.model.user.UserInfo
import site.dogether.domain.repository.UserRepository

class UserRepositoryImpl(private val httpClient: HttpClient) : UserRepository {

    override suspend fun loginWithKakao(
        name: String,
        idToken: String,
    ): Result<UserInfo> {
        return httpClient.safePost<KakaoLoginReq, UserInfoRes, UserInfo>(
            apiRoute = ApiRoutes.KAKAO_LOGIN,
            body = KakaoLoginReq(
                name = name,
                idToken = idToken
            ),
            mapper = UserInfoResMapper
        )
    }
}