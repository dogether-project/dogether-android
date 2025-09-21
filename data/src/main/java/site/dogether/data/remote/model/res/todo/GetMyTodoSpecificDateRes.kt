package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.todo.TodoResMapper.toDomainModel
import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo

@Serializable
data class GetMyTodoSpecificDateRes(
    val todos: List<TodoRes>
) : DataModel

object GetMyTodoSpecificDateResMapper : DataMapper<GetMyTodoSpecificDateRes, GetMyTodoSpecificDateInfo> {
    override fun GetMyTodoSpecificDateRes.toDomainModel(): GetMyTodoSpecificDateInfo {
        return GetMyTodoSpecificDateInfo(todos.map { it.toDomainModel() })
    }
}
