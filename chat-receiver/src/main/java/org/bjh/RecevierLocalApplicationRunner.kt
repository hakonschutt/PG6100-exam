package org.bjh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RecevierLocalApplicationRunner {


    fun main(args: Array<String>) {
        SpringApplication.run(RecevierLocalApplicationRunner::class.java, *args)

    }
}