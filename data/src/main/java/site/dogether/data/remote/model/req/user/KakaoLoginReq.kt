package site.dogether.data.remote.model.req.user

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginReq(
    val platform: String = PLATFORM_KAKAO,
    val name: String,
    val idToken: String,
) {
    companion object {
        private const val PLATFORM_KAKAO = "kakao"
    }
}