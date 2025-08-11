package site.dogether.data.remote.model.req.user

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginReq(
    val loginType: String = LOGIN_TYPE_KAKAO,
    val name: String,
    val providerId: String,
) {
    companion object {
        private const val LOGIN_TYPE_KAKAO = "KAKAO"
    }
}