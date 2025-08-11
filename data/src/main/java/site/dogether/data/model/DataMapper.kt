package site.dogether.data.model

import site.dogether.domain.model.DomainModel

interface DataMapper<in Data : DataModel, out Domain : DomainModel> {
    fun Data.toDomainModel(): Domain
}