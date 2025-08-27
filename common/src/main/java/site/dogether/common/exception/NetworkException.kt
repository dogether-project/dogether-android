package site.dogether.common.exception

class NetworkFailureException(
    val code: String,
    override val message: String,
) : Exception()