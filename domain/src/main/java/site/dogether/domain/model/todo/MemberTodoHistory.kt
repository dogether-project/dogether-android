package site.dogether.domain.model.todo

import site.dogether.domain.model.DomainModel

data class MemberTodoHistory(
    val currentTodoHistoryToReadIndex: Int = 0,
    val isMine: Boolean = false,
    val todos: List<Todo> = emptyList(),
) : DomainModel

