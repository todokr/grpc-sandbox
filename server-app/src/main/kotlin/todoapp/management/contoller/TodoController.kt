package todoapp.management.contoller

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
    fun create(@RequestBody command: CreateTodoCommand) = todoService.create(command)

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody command: UpdateTodoCommand) = todoService.update(id, command)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = todoService.delete(id)
}