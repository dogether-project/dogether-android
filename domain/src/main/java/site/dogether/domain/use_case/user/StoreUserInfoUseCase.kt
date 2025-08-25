package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class StoreUserInfoUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        name: String,
        accessToken: String
    ): Result<Unit> = repository.storeUserInfo(
        name = name,
        accessToken = accessToken
    )
}