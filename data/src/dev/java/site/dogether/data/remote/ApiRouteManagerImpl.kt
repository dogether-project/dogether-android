package site.dogether.data.remote

object ApiRouteManagerImpl : ApiRouteManager {
    override fun getBaseUrl(): String = "https://api-dev.dogether.site/api/v1/"
}