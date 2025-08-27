package site.dogether.domain.use_case.group

import site.dogether.domain.model.group.CreatedGroupInfo
import site.dogether.domain.repository.GroupRepository

class CreateGroupUseCase(private val repository: GroupRepository) {
    suspend operator fun invoke(
        name: String,
        maximumMemberCount: Int,
        isLaunchFromToday: Boolean,
        duration: Int
    ) : Result<CreatedGroupInfo> = repository.createGroup(
        name = name,
        maximumMemberCount = maximumMemberCount,
        isLaunchFromToday = isLaunchFromToday,
        duration = duration
    )
}