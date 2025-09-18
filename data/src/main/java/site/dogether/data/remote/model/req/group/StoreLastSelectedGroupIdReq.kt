package site.dogether.data.remote.model.req.group

import kotlinx.serialization.Serializable

@Serializable
data class StoreLastSelectedGroupIdReq(val groupId: Int)