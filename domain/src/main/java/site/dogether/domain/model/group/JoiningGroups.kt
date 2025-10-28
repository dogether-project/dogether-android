package site.dogether.domain.model.group

import site.dogether.domain.model.DomainModel

data class JoiningGroups(
    val lastSelectedGroupIndex: Int,
    val groups: List<Group>,
) : DomainModel
