package site.dogether.data.remote.model.req.todo

import kotlinx.serialization.Serializable

@Serializable
data class CreateTodoReq(
    val todos: List<String>
)