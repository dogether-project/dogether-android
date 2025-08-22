package site.dogether.domain.use_case.user

import site.dogether.domain.model.user.ParticipatingInfo
import site.dogether.domain.repository.UserRepository

class CheckParticipatingUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<ParticipatingInfo> = repository.checkParticipating()
}
