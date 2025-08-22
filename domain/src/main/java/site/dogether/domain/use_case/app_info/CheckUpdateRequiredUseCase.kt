package site.dogether.domain.use_case.app_info

import site.dogether.domain.model.app_invo.UpdateInfo
import site.dogether.domain.repository.AppInfoRepository

class CheckUpdateRequiredUseCase(private val repository: AppInfoRepository) {
    suspend operator fun invoke(appVersion: String): Result<UpdateInfo> = repository.checkUpdateRequired(appVersion)
}