package site.dogether.domain.use_case.user

import site.dogether.domain.repository.UserRepository

class WithdrawUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.withdraw()
}