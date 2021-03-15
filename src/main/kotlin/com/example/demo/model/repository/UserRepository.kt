package com.example.demo.model.repository

import com.example.demo.model.User
import io.vertx.core.Future
import io.vertx.kotlin.coroutines.await
import io.vertx.pgclient.PgPool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val client: PgPool) {

    suspend fun findAll(): Flow<ArrayList<User>> {
        return flowOf(client.connection
            .compose { connection ->
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
            }.onSuccess { count -> println("Fetched users is $count") }.onFailure { err -> println("Error during count $err") }.await())
    }

//    suspend fun findAll(): Flow<User> {
//
//    }
//
//    suspend fun deleteAll() =
//        client.execute().sql("DELETE FROM users").await()
//
//    suspend fun save(user: User) =
//        client.insert().into<User>().table("users").using(user).await()
//
//    suspend fun init() {
//        client.execute().sql("CREATE TABLE IF NOT EXISTS users (login varchar PRIMARY KEY, firstname varchar, lastname varchar);")
//            .await()
//        deleteAll()
//        save(User("smaldini", "Stéphane", "Maldini"))
//        save(User("sdeleuze", "Sébastien", "Deleuze"))
//        save(User("bclozel", "Brian", "Clozel"))
}