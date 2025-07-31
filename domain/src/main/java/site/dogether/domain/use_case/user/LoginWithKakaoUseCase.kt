package site.dogether.domain.use_case.user

import site.dogether.domain.model.user.UserInfo
import site.dogether.domain.repository.UserRepository

class LoginWithKakaoUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        name: String,
        idToken: String,
    ): Result<UserInfo> = repository.loginWithKakao(
        name = name,
        idToken = idToken
    )
}