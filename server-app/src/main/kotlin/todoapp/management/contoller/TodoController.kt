package todoapp.management.contoller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import todoapp.management.CreateTodoCommand
import todoapp.management.ListTodoQuery
import todoapp.management.TodoService
import todoapp.management.UpdateTodoCommand
import todoapp.management.model.Todo

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService
) {
    @GetMapping
    fun list(
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): List<Todo> = todoService.list(ListTodoQuery(limit = limit, offset = offset))

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): Todo? = todoService.find(id)

    @PostMapping
    fun create(@RequestBody command: CreateTodoCommand): ResponseEntity<Todo> {
        val todo = todoService.create(command)
        return ResponseEntity.ok(todo)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody command: UpdateTodoCommand): ResponseEntity<Todo> {
        val todo = todoService.update(id, command)
        return ResponseEntity.ok(todo)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = todoService.delete(id)
}