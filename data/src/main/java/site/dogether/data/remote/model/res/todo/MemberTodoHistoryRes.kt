package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.common.utils.orZero
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.todo.TodoResMapper.toDomainModel
import site.dogether.domain.model.todo.MemberTodoHistory

@Serializable
data class MemberTodoHistoryRes(
    val currentTodoHistoryToReadIndex: Int? = 0,
    val isMine: Boolean? = false,
    val todos: List<TodoRes> = emptyList(),
) : DataModel

object MemberTodoHistoryResMapper : DataMapper<MemberTodoHistoryRes, MemberTodoHistory> {
    override fun MemberTodoHistoryRes.toDomainModel(): MemberTodoHistory {
        return MemberTodoHistory(
            currentTodoHistoryToReadIndex = currentTodoHistoryToReadIndex.orZero(),
            isMine = isMine ?: false,
            todos = todos.map { it.toDomainModel() }
        )
    }
}

