package site.dogether.domain.use_case.todo

import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo
import site.dogether.domain.repository.TodoRepository

class GetMyTodoSpecificDateUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(
        groupId: Int,
        date: String
    ): Result<GetMyTodoSpecificDateInfo> = repository.getMyTodoSpecificDate(
        groupId = groupId,
        date = date
    )
}