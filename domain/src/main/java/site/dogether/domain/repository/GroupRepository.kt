package site.dogether.domain.repository

import site.dogether.domain.model.group.CreatedGroupInfo
import site.dogether.domain.model.group.ParticipateGroupInfo

interface GroupRepository {

    suspend fun createGroup(
        name: String,
        maximumMemberCount: Int,
        isLaunchFromToday: Boolean,
        duration: Int,
    ): Result<CreatedGroupInfo>

    suspend fun participateGroup(joinCode: String): Result<ParticipateGroupInfo>
}