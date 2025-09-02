package site.dogether.data.remote

object ApiRoutes {
    /* prod */
//    const val BASE_URL = "https://api-prod.dogether.site/api/v2/"

    /* dev */
    const val BASE_URL = "https://api-dev.dogether.site/api/v1/"
//    const val BASE_URL = "https://api-dev.dogether.site/api"

    /* Routes */
    const val KAKAO_LOGIN = "auth/login"
    const val CHECK_UPDATE_REQUIRED = "app-info/force-update-check"
    const val CHECK_PARTICIPATING = "groups/participating"
    const val CREATE_GROUP = "groups"
    const val PARTICIPATE_GROUP = "groups/join"
}