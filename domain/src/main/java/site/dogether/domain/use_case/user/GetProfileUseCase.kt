package site.dogether.domain.use_case.user

import site.dogether.domain.model.user.Profile
import site.dogether.domain.repository.UserRepository

class GetProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Profile> = repository.getProfile()
}