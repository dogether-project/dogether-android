package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.todo.Todo

@Serializable
data class TodoRes(
    val id: Long,
    val content: String,
    val status: String,
    val certificationContent: String,
    val certificationMediaUrl: String
) : DataModel

object TodoResMapper: DataMapper<TodoRes, Todo> {
    override fun TodoRes.toDomainModel(): Todo {
        return Todo(
            id = id,
            content = content,
            status = status,
            certificationContent = certificationContent,
            certificationMediaUrl = certificationMediaUrl
        )
    }
}
