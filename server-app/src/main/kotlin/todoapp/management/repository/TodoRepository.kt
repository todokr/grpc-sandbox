package todoapp.management.repository

import todoapp.management.model.Todo

interface TodoRepository {
    fun findById(id: String): Todo?
    fun store(todo: Todo)
    fun delete(id: String)
}