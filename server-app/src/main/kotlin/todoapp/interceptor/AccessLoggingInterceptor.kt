package todoapp.interceptor

import io.grpc.*
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.Duration
import net.logstash.logback.marker.Markers

@Component
@GrpcGlobalServerInterceptor
class ExceptionFilter(val clock: Clock) : ServerInterceptor {

    private val logger = LoggerFactory.getLogger("access-log")

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        meta: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {

        val accessLog = AccessLog(clock, call)
        return next.startCall(
            LoggingCallListener(call) { status ->
                val finishedLog = accessLog.finish(LocalDateTime.now(clock), status)
                logger.info(
                    Markers.appendEntries(finishedLog.toMap()),
                    "Request finished: {}, status: {}",
                    finishedLog.rpc,
                    status.code.name
                )
            },
            meta
        )
    }

    internal class LoggingCallListener<ReqT, RespT>(
        call: ServerCall<ReqT, RespT>,
        private val onFinished: (Status) -> Unit,
    ): ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {

        override fun close(status: Status, trailers: Metadata) {
            onFinished(status)
            super.close(status, trailers)
        }
    }
}

private class AccessLog private constructor(
    val rpc: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime? = null,
    val status: Status? = null,
) {
    val elapsedTime: Duration? = endAt?.let { Duration.between(startAt, it) }

    fun finish(endAt: LocalDateTime, status: Status) =
        AccessLog(rpc, startAt, endAt, status)

    fun toMap(): Map<String, Any?> =
        mapOf(
            // Datadog Logs の 標準属性
            "duration" to elapsedTime?.toNanos(),
            // その他
            "access_log" to
                    mapOf(
                        "startAt" to startAt.toString(),
                        "endAt" to endAt?.toString(),
                        "elapsedMillis" to elapsedTime?.toMillis(),
                        "rpc" to rpc,
                        "status" to mapOf("code" to status?.code?.name, "number" to status?.code?.value()),
                    ),
        )

    companion object {
        operator fun invoke(
            clock: Clock,
            call: ServerCall<*, *>
        ): AccessLog {
            val rpc: String = call.methodDescriptor.fullMethodName
            val startAt: LocalDateTime = LocalDateTime.now(clock)
            return AccessLog(rpc, startAt)
        }
    }
}