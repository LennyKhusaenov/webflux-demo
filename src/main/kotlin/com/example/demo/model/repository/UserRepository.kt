package com.example.demo.model.repository

import com.example.demo.model.Pet
import com.example.demo.model.User
import io.vertx.kotlin.coroutines.await
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val client: PgPool) : Logging {

    suspend fun findAll(): Flow<ArrayList<User>> {
        return flowOf(client.withConnection { connection ->
            connection
                .query("SELECT * FROM users")
                .execute()
                .map { rows ->
                    val users = ArrayList<User>()
                    rows.forEach { row ->
                        users.add(User(row.getLong("id"), row.getString("name"), row.getInteger("age")))
                    }
                    return@map users
                }
                .eventually { connection.close() }
        }.onSuccess { count -> logger.info("Fetched users are $count") }
            .onFailure { err -> logger.error("Error during count $err") }
            .await())
    }

    suspend fun saveUser(user: User): Flow<User> {
        return flowOf(client.withConnection { connection ->
            connection.preparedQuery("insert into users values ($1,$2,$3)")
                .execute(Tuple.of(user.id, user.age, user.name))
                .map { rows ->
                    rows.forEach { row ->
                        logger.info(row)
                    }
                    return@map user
                }.eventually { connection.close() }
        }.onSuccess { count -> println("Fetched users are $count") }
            .onFailure { err -> logger.error("Error during count $err") }
            .await())
    }

    suspend fun saveUserWithPet(user: User) {
        val pet = Pet()
         client.getConnection()
            .onSuccess { conn ->
                conn.begin()
                    .compose { tx ->
                        conn
                            .preparedQuery("insert into users VALUES ($1,$2, $3)")
                            .execute(Tuple.of(user.id, user.age, user.name))
                            .compose {
                                conn
                                    .preparedQuery("insert into  pets  VALUES ($1, $2, $3)")
                                    .execute(Tuple.of(pet.id, pet.name, pet.userId))
                            }
                            .compose { tx.commit() }
                    }
                    .eventually { conn.close() }
                    .onSuccess { logger.info("Transaction succeeded") }
                    .onFailure { err -> logger.error("Transaction failed: $err") }
            }
    }
}