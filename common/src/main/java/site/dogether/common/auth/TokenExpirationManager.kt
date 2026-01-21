package site.dogether.common.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 토큰 만료 이벤트를 관리하는 싱글톤 객체
 * 네트워크 통신 시 토큰 만료 에러가 발생하면 이벤트를 발생시키고,
 * UI 레이어에서 이를 구독하여 로그인 화면으로 이동시킵니다.
 */
object TokenExpirationManager {

    // 토큰 만료로 판단할 에러 코드들
    private val TOKEN_EXPIRED_CODES = setOf(
        "ATF-0001",
        "ATF-0003"
    )

    private val _tokenExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val tokenExpiredEvent: SharedFlow<Unit> = _tokenExpiredEvent.asSharedFlow()

    fun notifyTokenExpired() {
        _tokenExpiredEvent.tryEmit(Unit)
    }

    fun isTokenExpiredError(code: String): Boolean {
        return TOKEN_EXPIRED_CODES.contains(code)
    }
}

