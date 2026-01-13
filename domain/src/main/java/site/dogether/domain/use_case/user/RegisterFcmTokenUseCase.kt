package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class RegisterFcmTokenUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return repository.registerFcmToken(token)
    }
}