package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class CreateMyTodosUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(
        groupId: Int,
        todos: List<String>
    ): Result<Unit> = repository.createMyTodo(
        groupId = groupId,
        todos = todos
    )
}