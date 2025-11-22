package site.dogether.domain.use_case.user

import site.dogether.domain.model.user.GroupStatistics
import site.dogether.domain.repository.UserRepository

class GetGroupStatisticsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(groupId: Int): Result<GroupStatistics> = repository.getGroupStatistics(groupId)
}