package todoapp.management.query

interface FilterInterpreter<R> {

    fun interpret(filter: Filter): R {
        return when (filter) {
            is Filter.UnaryFilter -> interpretUnaryFilter(filter)
            is Filter.AndFilter -> interpretAndFilter(filter)
            is Filter.OrFilter -> interpretOrFilter(filter)
        }
    }

    fun interpretUnaryFilter(filter: Filter.UnaryFilter): R

    fun interpretAndFilter(filter: Filter.AndFilter): R

    fun interpretOrFilter(filter: Filter.OrFilter): R
}