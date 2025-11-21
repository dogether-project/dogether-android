package site.dogether.domain.use_case.group

import site.dogether.domain.model.user.RankingMembers
import site.dogether.domain.repository.GroupRepository

class GetRankingUseCase(private val repository: GroupRepository) {
    suspend operator fun invoke(groupId: Int): Result<RankingMembers> = repository.getRanking(groupId)
}