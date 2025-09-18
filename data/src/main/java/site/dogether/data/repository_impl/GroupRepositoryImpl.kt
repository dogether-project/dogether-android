package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.group.CreateGroupReq
import site.dogether.data.remote.model.req.group.ParticipateGroupReq
import site.dogether.data.remote.model.req.group.StoreLastSelectedGroupIdReq
import site.dogether.data.remote.model.res.group.CreateGroupResMapper
import site.dogether.data.remote.model.res.group.GetJoiningGroupsResMapper
import site.dogether.data.remote.model.res.group.ParticipateGroupResMapper
import site.dogether.data.utils.safeGet
import site.dogether.data.utils.safePost
import site.dogether.data.utils.safePostWithoutRes
import site.dogether.domain.model.group.CreatedGroupInfo
import site.dogether.domain.model.group.JoiningGroups
import site.dogether.domain.model.group.ParticipateGroupInfo
import site.dogether.domain.repository.GroupRepository

class GroupRepositoryImpl(private val httpClient: HttpClient) : GroupRepository {

    override suspend fun createGroup(
        name: String,
        maximumMemberCount: Int,
        isLaunchFromToday: Boolean,
        duration: Int,
    ): Result<CreatedGroupInfo> {
        return httpClient.safePost(
            apiRoute = ApiRoutes.CREATE_GROUP,
            body = CreateGroupReq(
                groupName = name,
                maximumMemberCount = maximumMemberCount,
                startAt = if (isLaunchFromToday) CreateGroupReq.START_AT_TODAY else CreateGroupReq.START_AT_TOMORROW,
                duration = duration
            ),
            mapper = CreateGroupResMapper
        )
    }

    override suspend fun participateGroup(joinCode: String): Result<ParticipateGroupInfo> {
        return httpClient.safePost(
            apiRoute = ApiRoutes.PARTICIPATE_GROUP,
            body = ParticipateGroupReq(joinCode),
            mapper = ParticipateGroupResMapper
        )
    }

    override suspend fun getJoiningGroups(): Result<JoiningGroups> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.GET_JOINING_GROUPS,
            mapper = GetJoiningGroupsResMapper
        )
    }

    override suspend fun storeLastSelectedGroupId(groupId: Int): Result<Unit> {
        return httpClient.safePostWithoutRes(
            apiRoute = ApiRoutes.STORE_LAST_SELECTED_GROUP_ID,
            body = StoreLastSelectedGroupIdReq(groupId)
        )
    }
}