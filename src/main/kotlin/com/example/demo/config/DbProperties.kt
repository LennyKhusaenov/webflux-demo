package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "demo.db")
data class DbProperties(val host: String, val port: Int, val database: String, val username: String, val password: String) {
}