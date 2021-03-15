package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.model.repository.UserRepository
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.json

@Service
class UsersHandler @Autowired constructor(private val userRepo: UserRepository) {
    suspend fun findAll(serverRequest: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyAndAwait(userRepo.findAll())

    suspend fun save(serverRequest: ServerRequest): ServerResponse {
        return ServerResponse.ok().json()
            .bodyAndAwait(userRepo.saveUser(serverRequest.body(BodyExtractors.toMono(User::class.java)).awaitFirst()))
    }

    suspend fun saveWithDefaultPer(serverRequest: ServerRequest): ServerResponse {
        userRepo.saveUserWithPet(serverRequest.body(BodyExtractors.toMono(User::class.java)).awaitFirst())
        return findAll(serverRequest)
    }
}