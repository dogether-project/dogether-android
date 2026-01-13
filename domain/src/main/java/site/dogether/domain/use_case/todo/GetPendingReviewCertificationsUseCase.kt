package site.dogether.domain.use_case.todo

import site.dogether.domain.model.todo.PendingReviewCertifications
import site.dogether.domain.repository.TodoRepository

class GetPendingReviewCertificationsUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(): Result<PendingReviewCertifications> {
        return repository.getPendingReviewCertifications()
    }
}

