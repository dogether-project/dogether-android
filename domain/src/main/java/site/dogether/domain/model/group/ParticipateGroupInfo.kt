package site.dogether.domain.model.group

import site.dogether.domain.model.DomainModel

data class ParticipateGroupInfo(
    val groupName: String,
    val duration: Int,
    val maximumMemberCount: Int,
    val startAt: String,
    val endAt: String,
) : DomainModel
