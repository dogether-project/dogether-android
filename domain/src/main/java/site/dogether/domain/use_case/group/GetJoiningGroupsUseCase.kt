package site.dogether.domain.use_case.group

import site.dogether.domain.model.group.JoiningGroups
import site.dogether.domain.repository.GroupRepository

class GetJoiningGroupsUseCase(private val repository: GroupRepository) {
    suspend operator fun invoke(): Result<JoiningGroups> = repository.getJoiningGroups()
}