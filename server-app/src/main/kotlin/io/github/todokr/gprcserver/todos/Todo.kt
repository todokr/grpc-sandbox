package io.github.todokr.gprcserver.todos

enum class Progress {
    TODO,
    DOING,
    DONE
}

data class Todo(
    val id: Long,
    val title: String,
    val description: String,
    val progress: Progress
)