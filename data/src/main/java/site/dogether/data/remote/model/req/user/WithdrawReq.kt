package site.dogether.data.remote.model.req.user

import kotlinx.serialization.Serializable
import site.dogether.data.utils.LOGIN_TYPE

@Serializable
data class WithdrawReq(
    val loginType: String = LOGIN_TYPE
)