package todoapp.management.query

import io.github.todokr.jooq.tables.Todos
import org.jooq.Condition
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

/** for JOOQ Condition */
@Component
class FilterInterpreterImpl: FilterInterpreter<Condition> {
    private fun column(property: TodoProperty): org.jooq.Field<String> {
        return when (property) {
            TodoProperty.ID -> Todos.TODOS.ID
            TodoProperty.TITLE -> Todos.TODOS.TITLE
            TodoProperty.DESCRIPTION -> Todos.TODOS.DESCRIPTION
            TodoProperty.PROGRESS -> Todos.TODOS.PROGRESS
        }
    }

    override fun interpretUnaryFilter(filter: Filter.UnaryFilter): Condition {
        return when (val predicate = filter.predicate) {
            is Predicate.EqPredicate -> column(predicate.property).eq(predicate.value)
            is Predicate.NeqPredicate -> column(predicate.property).ne(predicate.value)
            is Predicate.InPredicate -> column(predicate.property).`in`(predicate.values)
            is Predicate.NotInPredicate -> column(predicate.property).notIn(predicate.values)
        }
    }

    override fun interpretAndFilter(filter: Filter.AndFilter): Condition {
        return DSL.and(filter.filters.map { interpret(it) })
    }

    override fun interpretOrFilter(filter: Filter.OrFilter): Condition {
        return DSL.or(filter.filters.map { interpret(it) })
    }
}