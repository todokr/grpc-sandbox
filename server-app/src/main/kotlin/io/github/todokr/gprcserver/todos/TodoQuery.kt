package io.github.todokr.gprcserver.todos

import org.springframework.stereotype.Repository

interface TodoQuery {

    fun list(criteria: Criteria): List<Todo>
}

data class Criteria(
    val tenantId: String,
    val filter: Filter,
    val limit: Int,
    val offset: Int
)

enum class Item {
    ID,
    TITLE,
    DESCRIPTION,
    PROGRESS,
}

sealed interface Predicate {
    data class EqPredicate(val item: Item, val value: String): Predicate
    data class NeqPredicate(val item: Item, val value: String): Predicate
    data class InPredicate(val item: Item, val values: List<String>): Predicate
    data class NotInPredicate(val item: Item, val values: List<String>): Predicate
}

sealed interface Filter {
    data class UnaryFilter(val predicate: Predicate): Filter
    data class AndFilter(val filters: List<Filter>): Filter
    data class OrFilter(val filters: List<Filter>): Filter
}

@Repository
class TodoQueryImpl: TodoQuery {
    override fun list(criteria: Criteria): List<Todo> {
        return listOf(
            Todo(
                id = "123",
                title = "title",
                description = "description",
                progress = Progress.TODO,
            )
        )
    }
}