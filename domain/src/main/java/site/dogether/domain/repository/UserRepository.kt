package site.dogether.domain.repository

import site.dogether.domain.model.group.Group
import site.dogether.domain.model.user.GroupStatistics
import site.dogether.domain.model.user.ParticipatingInfo
import site.dogether.domain.model.user.UserInfo

interface UserRepository {

    suspend fun storeUserInfo(
        name: String,
        accessToken: String,
    ): Result<Unit>

    suspend fun getUserInfo(): Result<UserInfo>

    suspend fun checkParticipating(): Result<ParticipatingInfo>

    suspend fun loginWithKakao(
        name: String,
        idToken: String,
    ): Result<UserInfo>

    suspend fun clearUserInfo()

    suspend fun withdraw(): Result<Unit>

    suspend fun getGroupStatistics(groupId: Int): Result<GroupStatistics>
}