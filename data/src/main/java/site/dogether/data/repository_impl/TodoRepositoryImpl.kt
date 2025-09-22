package site.dogether.data.repository_impl

import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.todo.CreateTodoReq
import site.dogether.data.remote.model.res.todo.GetMyTodoSpecificDateResMapper
import site.dogether.data.utils.safeGet
import site.dogether.data.utils.safePostWithoutRes
import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo
import site.dogether.domain.repository.TodoRepository

class TodoRepositoryImpl(private val httpClient: HttpClient) : TodoRepository {
    override suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String
    ): Result<GetMyTodoSpecificDateInfo> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.getMyTodoSpecificDate(groupId),
            params = mapOf("date" to date.replace(".", "-")),
            mapper = GetMyTodoSpecificDateResMapper
        )
    }

    override suspend fun createMyTodo(groupId: Int, todos: List<String>): Result<Unit> {
        return httpClient.safePostWithoutRes(
            apiRoute = ApiRoutes.createMyTodos(groupId = groupId),
            body = CreateTodoReq(todos = todos),
        )
    }
}