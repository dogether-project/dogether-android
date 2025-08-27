package site.dogether.data.remote.model.req.group

import kotlinx.serialization.Serializable


@Serializable
data class CreateGroupReq(
    val groupName: String,
    val maximumMemberCount: Int,
    val startAt: String,
    val duration: Int,
) {
    companion object {
        const val START_AT_TODAY = "TODAY"
        const val START_AT_TOMORROW = "TOMORROW"
    }
}