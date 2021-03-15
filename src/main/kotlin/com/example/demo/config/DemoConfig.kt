package com.example.demo.config

import com.example.demo.service.UsersHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class DemoConfig @Autowired constructor(private val usersHandler: UsersHandler) {

    @Bean
    fun findUsersRoute() =
        coRouter {
            GET("/users", usersHandler::findAll)
            POST("/users", usersHandler::save)
            POST("/user/with/pet", usersHandler::saveWithDefaultPer)
        }
}