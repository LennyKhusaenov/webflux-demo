package com.example.demo.config

import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(DbProperties::class)
class DataBaseConfig(private val dbProperties: DbProperties) {

    @Bean
    fun pgPool(): PgPool {
        val pgConnectOptions = PgConnectOptions().setPort(5432)
            .setHost(dbProperties.host)
            .setDatabase(dbProperties.database)
            .setUser(dbProperties.username)
            .setPassword(dbProperties.password);
        val poolOptions = PoolOptions()
            .setMaxSize(5);
        return PgPool.pool(pgConnectOptions, poolOptions);
    }
}