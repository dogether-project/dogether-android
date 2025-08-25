package site.dogether.domain.use_case.user

import site.dogether.domain.model.user.UserInfo
import site.dogether.domain.repository.UserRepository

class GetUserInfoUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<UserInfo> = repository.getUserInfo()
}