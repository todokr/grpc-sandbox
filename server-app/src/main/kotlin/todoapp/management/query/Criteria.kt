package todoapp.management.query

import todoapp.management.model.TodoProgress


data class Criteria(
    val filter: Filter = Filter.Default,
    val limit: Int,
    val offset: Int
)

enum class TodoProperty {
    ID,
    TITLE,
    DESCRIPTION,
    PROGRESS,
}

sealed interface Predicate {
    // TODO: Generalize the property type
    data class EqPredicate(val property: TodoProperty, val value: String): Predicate
    data class NeqPredicate(val property: TodoProperty, val value: String): Predicate
    data class InPredicate(val property: TodoProperty, val values: List<String>): Predicate
    data class NotInPredicate(val property: TodoProperty, val values: List<String>): Predicate
}

sealed interface Filter {
    data class UnaryFilter(val predicate: Predicate): Filter
    data class AndFilter(val filters: List<Filter>): Filter
    data class OrFilter(val filters: List<Filter>): Filter

    companion object {
        val Default = UnaryFilter(
            Predicate.EqPredicate(
                TodoProperty.PROGRESS,
                TodoProgress.TODO.code
            )
        )
    }
}