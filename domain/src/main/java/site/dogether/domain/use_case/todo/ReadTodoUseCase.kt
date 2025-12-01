package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class ReadTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(todoId: Long): Result<Unit> = repository.readTodo(todoId)
}