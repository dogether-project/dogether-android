package site.dogether.domain.repository

import site.dogether.domain.model.user.UserInfo

interface UserRepository {
    suspend fun getUserToken(): Result<String>

    suspend fun loginWithKakao(
        name: String,
        idToken: String,
    ): Result<UserInfo>
}