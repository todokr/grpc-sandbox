package todoapp.externalapi


import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

import todoapp.management.model.Todo
import todoapp.management.model.TodoProgress
import todoapp.management.query.Criteria
import todoapp.management.query.Filter
import todoapp.proto.Todo as ProtoTodo
import todoapp.proto.Progress as ProtoProgress
import todoapp.management.query.TodoQuery
import todoapp.management.repository.TodoRepository
import todoapp.proto.FindTodoRequest
import todoapp.proto.FindTodoResponse
import todoapp.proto.ListTodosRequest
import todoapp.proto.ListTodosResponse
import todoapp.proto.TodoServiceGrpc.TodoServiceImplBase

@GrpcService
class GrpcTodoService(
    private val todoRepository: TodoRepository,
    private val todoQuery: TodoQuery
): TodoServiceImplBase() {

    override fun findTodo(
        request: FindTodoRequest,
        responseObserver: StreamObserver<FindTodoResponse>
    ) {
        val todo = todoRepository.findById(request.id)
        if (todo == null) {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException())
        } else {
            val response = FindTodoResponse.newBuilder()
                .setTodo(asProtoTodo(todo))
                .build()
            responseObserver.onNext(response)
            responseObserver.onCompleted()
        }
    }

    override fun listTodos(
        request: ListTodosRequest,
        responseObserver: StreamObserver<ListTodosResponse>
    ) {
        val criteria = Criteria(
            filter = Filter.Default,
            limit = 10,
            offset = 0
        )
        val todos = todoQuery.list(criteria)
        val response = ListTodosResponse.newBuilder()
            .addAllTodos(todos.map { asProtoTodo(it) })
            .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun asProtoProgress(progress: TodoProgress): ProtoProgress {
        return when (progress) {
            TodoProgress.TODO -> ProtoProgress.PROGRESS_TODO
            TodoProgress.DOING -> ProtoProgress.PROGRESS_DOING
            TodoProgress.DONE -> ProtoProgress.PROGRESS_DONE
        }
    }

    private fun asProtoTodo(todo: Todo): ProtoTodo {
        return ProtoTodo.newBuilder()
            .setId(todo.id)
            .setTitle(todo.title)
            .setDescription(todo.description)
            .setProgress(asProtoProgress(todo.progress))
            .build()
    }
}
