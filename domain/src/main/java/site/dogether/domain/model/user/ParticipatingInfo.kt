package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class ParticipatingInfo(
    val shouldParticipating: Boolean,
) : DomainModel
