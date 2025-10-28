package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class CertificateTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(
        dailyTodoId: Int,
        content: String,
        mediaUrl: String
    ): Result<Unit> {
        return repository.certifyTodo(
            dailyTodoId = dailyTodoId,
            content = content,
            mediaUrl = mediaUrl
        )
    }
}
