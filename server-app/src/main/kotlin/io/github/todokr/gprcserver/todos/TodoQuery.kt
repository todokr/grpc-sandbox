package io.github.todokr.gprcserver.todos

interface TodoQuery {

    fun list()
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