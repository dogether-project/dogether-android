package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class RemindTodoUseCase(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(
        todoId: Long,
        reminderType: String
    ): Result<Unit> {
        return repository.remindTodo(
            todoId = todoId,
            reminderType = reminderType
        )
    }
}
