package site.dogether.data.remote.model.res.app_info

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.app_info.UpdateInfo

@Serializable
data class CheckUpdateRequiredRes(
    val forceUpdateRequired: Boolean,
) : DataModel

object CheckUpdateRequiredMapper : DataMapper<CheckUpdateRequiredRes, UpdateInfo> {

    override fun CheckUpdateRequiredRes.toDomainModel(): UpdateInfo {
        return UpdateInfo(isForceUpdateRequired = forceUpdateRequired)
    }
}
