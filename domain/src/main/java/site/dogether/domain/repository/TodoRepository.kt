package site.dogether.domain.repository

import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo

interface TodoRepository {
    suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String
    ): Result<GetMyTodoSpecificDateInfo>
}