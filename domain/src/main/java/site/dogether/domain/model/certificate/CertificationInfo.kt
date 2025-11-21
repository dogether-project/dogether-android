package site.dogether.domain.model.certificate

import site.dogether.domain.model.DomainModel

data class CertificationInfo(
    val id: Long,
    val content: String,
    val status: String,
    val certificationContent: String,
    val certificationMediaUrl: String,
    val reviewFeedBack: String
) : DomainModel
