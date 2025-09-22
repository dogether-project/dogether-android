package site.dogether.domain.repository

import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo

interface TodoRepository {
    suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String
    ): Result<GetMyTodoSpecificDateInfo>

    suspend fun createMyTodo(groupId: Int, todos: List<String>): Result<Unit>
}