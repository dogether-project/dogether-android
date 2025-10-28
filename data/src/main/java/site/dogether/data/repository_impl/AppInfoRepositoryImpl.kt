package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.res.app_info.CheckUpdateRequiredMapper
import site.dogether.data.remote.model.res.app_info.CheckUpdateRequiredRes
import site.dogether.data.utils.safeGet
import site.dogether.domain.model.app_info.UpdateInfo
import site.dogether.domain.repository.AppInfoRepository

class AppInfoRepositoryImpl(val httpClient: HttpClient) : AppInfoRepository {

    override suspend fun checkUpdateRequired(appVersion: String): Result<UpdateInfo> {
        return httpClient.safeGet<CheckUpdateRequiredRes, UpdateInfo>(
            apiRoute = ApiRoutes.CHECK_UPDATE_REQUIRED,
            params = mapOf("app-version" to appVersion),
            mapper = CheckUpdateRequiredMapper
        )
    }
}