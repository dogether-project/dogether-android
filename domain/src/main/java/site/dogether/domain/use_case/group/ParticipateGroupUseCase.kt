package site.dogether.domain.use_case.group

import site.dogether.domain.model.group.ParticipateGroupInfo
import site.dogether.domain.repository.GroupRepository

class ParticipateGroupUseCase(val repository: GroupRepository) {
    suspend operator fun invoke(joinCode: String): Result<ParticipateGroupInfo> = repository.participateGroup(joinCode)
}