package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class GetUserTokenUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<String> = repository.getUserToken()
}