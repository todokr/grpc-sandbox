package todoapp.management.model

enum class TodoProgress(val code: String) {
    TODO("TODO"),
    DOING("DOING"),
    DONE("DONE");

    companion object {
        fun fromCode(value: String): TodoProgress {
            return when (value) {
                "TODO" -> TODO
                "DOING" -> DOING
                "DONE" -> DONE
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}