package todoapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean(name = ["primary"])
    @ConfigurationProperties(prefix = "spring.primary-datasource")
    fun primaryDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean(name = ["replica"])
    @ConfigurationProperties(prefix = "spring.replica-datasource")
    fun replicaDataSource(): DataSource = DataSourceBuilder.create().build()

    @DependsOn("primary", "replica")
    @Primary
    @Bean
    fun datasource(
        primary: DataSource,
        replica: DataSource
    ): DataSource = RoutingDataSourceProxy(primary, replica)
}

private class RoutingDataSourceProxy (
    primaryDataSource: DataSource,
    replicaDataSource: DataSource,
) : LazyConnectionDataSourceProxy() {

    init {
        val routingDataSource = RoutingDataSource
        routingDataSource.setDefaultTargetDataSource(primaryDataSource)
        routingDataSource.setTargetDataSources(
            mapOf(
                DataSourceDestination.PRIMARY to primaryDataSource,
                DataSourceDestination.REPLICA to replicaDataSource,
            )
        )
        routingDataSource.afterPropertiesSet()
        targetDataSource = routingDataSource
    }
}

private enum class DataSourceDestination {
    PRIMARY,
    REPLICA,
}

private object RoutingDataSource: AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): Any? {
        val isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        return if (isReadOnly) DataSourceDestination.REPLICA else DataSourceDestination.PRIMARY
    }

}