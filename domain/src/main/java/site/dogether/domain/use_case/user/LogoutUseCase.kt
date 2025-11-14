package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class LogoutUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.clearUserInfo()
}