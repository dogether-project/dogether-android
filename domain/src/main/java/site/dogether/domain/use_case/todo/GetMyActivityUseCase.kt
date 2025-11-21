package site.dogether.domain.use_case.todo

import site.dogether.domain.model.todo.MyActivity
import site.dogether.domain.repository.TodoRepository

class GetMyActivityUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(
        sortBy: String,
        status: String? = null,
        page: Int = 0
    ): Result<MyActivity> = repository.getMyActivity(
        sortBy = sortBy,
        status = status,
        page = page
    )
}