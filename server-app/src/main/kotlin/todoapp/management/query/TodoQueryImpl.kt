package todoapp.management.query

import io.github.todokr.jooq.tables.Todos.TODOS
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import todoapp.DataAccessLogger
import todoapp.management.model.Todo
import todoapp.management.model.TodoProgress

@Component
class TodoQueryImpl(
    private val dslContext: DSLContext,
    private val filterInterpreter: FilterInterpreter<Condition>
): TodoQuery {
    private val logger = DataAccessLogger()

    override fun list(criteria: Criteria): List<Todo> {
        val condition = filterInterpreter.interpret(criteria.filter)
        val result = dslContext.selectFrom(TODOS)
            .where(condition)
            .limit(criteria.limit)
            .offset(criteria.offset)
            .fetch()
            .map { record ->
                Todo(
                    id = record.id,
                    title = record.title,
                    description = record.description,
                    progress = TodoProgress.fromCode(record.progress)
                )
            }
        logger.logQuery("Todo", result.size)
        return result
    }
}

