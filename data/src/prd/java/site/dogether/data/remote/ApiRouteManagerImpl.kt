package site.dogether.data.remote

object ApiRouteManagerImpl : ApiRouteManager {
    override fun getBaseUrl(): String = "https://api-prod.dogether.site/api/"
}