package com.funnyjack.monolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MonolithServiceApplication

fun main(args: Array<String>) {
    runApplication<MonolithServiceApplication>(*args)
}
