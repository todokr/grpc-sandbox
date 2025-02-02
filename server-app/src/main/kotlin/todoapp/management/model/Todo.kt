package todoapp.management.model

data class Todo(
    val id: String,
    val title: String,
    val description: String,
    val progress: TodoProgress
)