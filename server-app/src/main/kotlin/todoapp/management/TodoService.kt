package todoapp.management

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import todoapp.IdGenerator
import todoapp.management.model.Todo
import todoapp.management.model.TodoProgress
import todoapp.management.query.*
import todoapp.management.repository.TodoRepository

@Service
class TodoService (
    private val todoRepository: TodoRepository,
    private val todoQuery: TodoQuery,
    private val idGenerator: IdGenerator
) {
    @Transactional(readOnly = true)
    fun list(query: ListTodoQuery): List<Todo> {
        val criteria = Criteria(
            limit = query.limit,
            offset = query.offset
        )
        return todoQuery.list(criteria)
    }

    @Transactional(readOnly = true)
    fun find(todoId: String): Todo? =
        todoRepository.findById(todoId)

    @Transactional
    fun create(command: CreateTodoCommand) {
        val todo = Todo(
            id = idGenerator.gen(),
            title = command.title,
            description = command.description,
            progress = TodoProgress.TODO
        )
        todoRepository.store(todo)
    }

    @Transactional
    fun update(todoId: String, command: UpdateTodoCommand) {
        val todo = Todo(
            id = todoId,
            title = command.title,
            description = command.description,
            progress = command.progress
        )
        todoRepository.store(todo)
    }

    @Transactional
    fun delete(todoId: String) {
        todoRepository.delete(todoId)
    }
}

data class CreateTodoCommand (
    val title: String,
    val description: String,
)

data class UpdateTodoCommand (
    val title: String,
    val description: String,
    val progress: TodoProgress
)

data class ListTodoQuery (
    val limit: Int,
    val offset: Int
)