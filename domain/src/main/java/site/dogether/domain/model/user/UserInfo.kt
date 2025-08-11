package site.dogether.domain.model.user

import site.dogether.domain.model.DomainModel

data class UserInfo(
    val name: String,
    val accessToken: String,
) : DomainModel
