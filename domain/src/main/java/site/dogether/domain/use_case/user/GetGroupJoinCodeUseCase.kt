package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class GetGroupJoinCodeUseCase(private val repository: UserRepository) {
    operator fun invoke(): String {
        return repository.joinCode.orEmpty()
    }
}

