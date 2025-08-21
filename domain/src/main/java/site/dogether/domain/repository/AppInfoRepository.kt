package site.dogether.domain.repository

import site.dogether.domain.model.app_invo.UpdateInfo

interface AppInfoRepository {
    suspend fun checkUpdateRequired(appVersion: String): Result<UpdateInfo>
}