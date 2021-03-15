package com.example.demo.service

import com.example.demo.model.repository.UserRepository
import io.vertx.kotlin.coroutines.await
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.*

@Service
class UsersHandler @Autowired constructor(private val userRepo: UserRepository) {
    suspend fun findAll(serverRequest: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyAndAwait(userRepo.findAll())
}