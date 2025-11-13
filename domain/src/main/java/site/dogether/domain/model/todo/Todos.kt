package site.dogether.domain.model.todo

import site.dogether.domain.model.DomainModel

data class Todos(
    val todos: List<Todo>
): DomainModel
