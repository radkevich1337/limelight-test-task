package com.example.limelighttesttask

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
class LimelightTestTaskApplication

fun main(args: Array<String>) {
    runApplication<LimelightTestTaskApplication>(*args)
}
