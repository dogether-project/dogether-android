package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.todo.TodoResMapper.toDomainModel
import site.dogether.domain.model.todo.Todos

@Serializable
data class TodosRes(
    val todos: List<TodoRes>,
) : DataModel

object TodosResMapper : DataMapper<TodosRes, Todos> {
    override fun TodosRes.toDomainModel(): Todos {
        return Todos(todos.map { it.toDomainModel() })
    }
}
