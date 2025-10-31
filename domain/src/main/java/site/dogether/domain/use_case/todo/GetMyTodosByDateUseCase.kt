package site.dogether.domain.use_case.todo

import site.dogether.domain.model.todo.Todo
import site.dogether.domain.repository.TodoRepository

class GetMyTodosByDateUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(
        groupId: Int,
        date: String,
    ): Result<List<Todo>> = repository.getMyTodosByDate(
        groupId = groupId,
        date = date
    )
}