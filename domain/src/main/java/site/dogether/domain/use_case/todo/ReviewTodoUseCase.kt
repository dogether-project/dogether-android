package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class ReviewTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(
        todoId: Int,
        isApprove: Boolean,
        feedback: String = "",
    ): Result<Unit> {
        return repository.reviewTodo(
            todoId = todoId,
            isApprove = isApprove,
            feedback = feedback
        )
    }
}

