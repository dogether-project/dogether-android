package site.dogether.common.exception

class NetworkFailureException(
    val code: Int,
    override val message: String,
) : Exception()