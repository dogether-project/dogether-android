package site.dogether.domain.model.group

import site.dogether.domain.model.DomainModel

data class Group(
    val id: Int = 0,
    val name: String = "",
    val currentMemberCount: Int = 0,
    val maximumMemberCount: Int = 0,
    val joinCode: String = "",
    val status: String = "",
    val startAt: String = "",
    val endAt: String = "",
    val progressDay: Int = 0,
    val progressRate: Float = 0f,
) : DomainModel {

    companion object {
        const val STATUS_READY = "READY"
        const val STATUS_RUNNING = "RUNNING"
        const val STATUS_D_DAY = "D_DAY"
        const val STATUS_FINISHED = "FINISHED"
    }
}