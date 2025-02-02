package todoapp.management.query

import todoapp.management.model.Todo

interface TodoQuery {
    fun list(criteria: Criteria): List<Todo>
}