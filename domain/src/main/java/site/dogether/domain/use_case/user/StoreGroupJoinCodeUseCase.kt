package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class StoreGroupJoinCodeUseCase(private val repository: UserRepository) {
    operator fun invoke(joinCode: String = "") {
        repository.joinCode = joinCode
    }
}

