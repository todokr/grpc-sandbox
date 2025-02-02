package todoapp.management.repository

import io.github.todokr.jooq.tables.Todos
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import todoapp.DataAccessLogger
import todoapp.management.model.Todo
import todoapp.management.model.TodoProgress

@Repository
class TodoRepositoryImpl(private val dslContext: DSLContext) : TodoRepository {
    private val logger = DataAccessLogger()

    override fun findById(id: String): Todo? {
        val result = dslContext.selectFrom(Todos.TODOS)
            .where(Todos.TODOS.ID.eq(id))
            .fetchOne()?.let { record ->
                Todo(
                    id = record.id,
                    title = record.title,
                    description = record.description,
                    progress = TodoProgress.fromCode(record.progress)
                )
            }
        logger.logQuery("Todo", result?.let { 1 } ?: 0)
        return result
    }



    override fun store(todo: Todo) {
        dslContext.insertInto(Todos.TODOS)
            .set(Todos.TODOS.ID, todo.id)
            .set(Todos.TODOS.TITLE, todo.title)
            .set(Todos.TODOS.DESCRIPTION, todo.description)
            .set(Todos.TODOS.PROGRESS, todo.progress.code)
            .onConflict(Todos.TODOS.ID)
            .doUpdate()
            .set(Todos.TODOS.TITLE, todo.title)
            .set(Todos.TODOS.DESCRIPTION, todo.description)
            .set(Todos.TODOS.PROGRESS, todo.progress.code)
            .execute()
        logger.logMutation("Todo", "Store")
    }


    override fun delete(id: String) {
        dslContext.deleteFrom(Todos.TODOS)
            .where(Todos.TODOS.ID.eq(id))
            .execute()
        logger.logMutation("Todo", "Delete")
    }
}