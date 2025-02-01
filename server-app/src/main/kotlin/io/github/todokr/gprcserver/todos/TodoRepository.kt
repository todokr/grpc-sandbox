package io.github.todokr.gprcserver.todos

interface TodoRepository {
    fun findById(id: Long): Todo?

    fun create(todo: Todo): Todo

    fun update(todo: Todo): Todo

    fun delete(id: Long)
}