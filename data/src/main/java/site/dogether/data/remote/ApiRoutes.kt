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
    const val GET_JOINING_GROUPS = "groups/my"
    const val STORE_LAST_SELECTED_GROUP_ID = "groups/last-selected"
    fun getMyTodoSpecificDate(groupId: Int): String = "challenge-groups/$groupId/my-todos"
    fun createMyTodos(groupId: Int): String = "challenge-groups/$groupId/todos"
    const val GET_PRESIGNED_URLS = "s3/presigned-urls"
    fun certifyTodo(dailyTodoId: Int): String = "todos/$dailyTodoId/certify"

}