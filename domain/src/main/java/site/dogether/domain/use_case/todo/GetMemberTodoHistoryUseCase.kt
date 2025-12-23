package site.dogether.domain.use_case.todo

import site.dogether.domain.model.todo.MemberTodoHistory
import site.dogether.domain.repository.TodoRepository

class GetMemberTodoHistoryUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(
        groupId: Int,
        memberId: Int,
    ): Result<MemberTodoHistory> = repository.getMemberTodoHistory(
        groupId = groupId,
        memberId = memberId
    )
}

