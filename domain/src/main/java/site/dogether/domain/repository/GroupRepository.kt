package site.dogether.domain.repository

import site.dogether.domain.model.group.CreatedGroupInfo

interface GroupRepository {

    suspend fun createGroup(
        name: String,
        maximumMemberCount: Int,
        isLaunchFromToday: Boolean,
        duration: Int,
    ): Result<CreatedGroupInfo>
}