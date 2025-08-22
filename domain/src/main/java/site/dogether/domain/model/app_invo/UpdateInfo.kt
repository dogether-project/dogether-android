package site.dogether.domain.model.app_invo

import site.dogether.domain.model.DomainModel

data class UpdateInfo(
    val isForceUpdateRequired: Boolean,
) : DomainModel
