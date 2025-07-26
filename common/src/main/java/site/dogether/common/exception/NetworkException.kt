package site.dogether.common.exception

class NetworkFailureException(
    val code: Int,
    override val message: String
) : Exception()

class NetworkErrorException(val throwable: Throwable) : Exception()