package todoapp

import org.slf4j.LoggerFactory

class DataAccessLogger {
    private val logger = LoggerFactory.getLogger("data-access")

    fun logMutation(entity: String, event: String) {
        logger.atInfo()
            .addKeyValue("entity", entity)
            .addKeyValue("event", event)
            .log("Mutation: $entity, event: $event")
    }

    fun logQuery(resource: String, fetchCount: Int) {
        logger.atDebug()
            .addKeyValue("resource", resource)
            .addKeyValue("fetchCount", fetchCount)
            .log("Query: $resource, fetchCount: $fetchCount")
    }
}