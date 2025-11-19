package site.dogether.data.remote.model.res.todo.certificate

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.certificate.CertificationInfo

@Serializable
data class CertificationInfoRes(
    val id: Long,
    val content: String,
    val status: String,
    val certificationContent: String,
    val certificationMediaUrl: String,
    val reviewFeedBack: String? = null
) : DataModel

object CertificationInfoResMapper : DataMapper<CertificationInfoRes, CertificationInfo> {
    override fun CertificationInfoRes.toDomainModel(): CertificationInfo {
        return CertificationInfo(
            id = id,
            content = content,
            status = status,
            certificationContent = certificationContent,
            certificationMediaUrl = certificationMediaUrl,
            reviewFeedBack = reviewFeedBack.orEmpty()
        )
    }
}
