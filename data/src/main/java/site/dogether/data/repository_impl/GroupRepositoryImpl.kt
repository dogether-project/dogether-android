package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.group.CreateGroupReq
import site.dogether.data.remote.model.res.group.CreateGroupResMapper
import site.dogether.data.utils.safePost
import site.dogether.domain.model.group.CreatedGroupInfo
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
}