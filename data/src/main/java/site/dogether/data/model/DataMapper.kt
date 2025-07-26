package site.dogether.data.model

import site.dogether.domain.model.DomainModel

interface DataMapper<in DATA : DataModel, out DOMAIN : DomainModel> {
    fun DATA.toDomainModel(): DOMAIN
}