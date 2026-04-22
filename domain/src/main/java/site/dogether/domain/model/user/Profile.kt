package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class Profile(
    val name: String = "",
    val profileImageUrl: String = ""
) : DomainModel