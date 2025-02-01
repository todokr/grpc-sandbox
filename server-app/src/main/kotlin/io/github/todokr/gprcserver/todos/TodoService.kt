package io.github.todokr.gprcserver.todos

import io.github.todokr.todo.TodoOuterClass
import io.github.todokr.todo.TodoServiceGrpc.TodoServiceImplBase
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service

@Service
class TodoService: TodoServiceImplBase() {

    override fun findTodo(
        request: TodoOuterClass.FindTodoRequest?,
        responseObserver: StreamObserver<TodoOuterClass.FindTodoResponse>?
    ) {
        super.findTodo(request, responseObserver)
    }

    override fun listTodos(
        request: TodoOuterClass.ListTodosRequest?,
        responseObserver: StreamObserver<TodoOuterClass.ListTodosResponse>?
    ) {
        super.listTodos(request, responseObserver)
    }
}