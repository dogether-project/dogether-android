package site.dogether.domain.repository

import site.dogether.domain.model.group.CreatedGroupInfo
import site.dogether.domain.model.group.JoiningGroups
import site.dogether.domain.model.group.ParticipateGroupInfo
import site.dogether.domain.model.user.RankingMembers

interface GroupRepository {

    suspend fun createGroup(
        name: String,
        maximumMemberCount: Int,
        isLaunchFromToday: Boolean,
        duration: Int,
    ): Result<CreatedGroupInfo>

    suspend fun participateGroup(joinCode: String): Result<ParticipateGroupInfo>

    suspend fun getJoiningGroups(): Result<JoiningGroups>

    suspend fun storeLastSelectedGroupId(groupId: Int): Result<Unit>

    suspend fun withdrawGroup(groupId: Int): Result<Unit>

    suspend fun getRanking(groupId: Int): Result<RankingMembers>
}