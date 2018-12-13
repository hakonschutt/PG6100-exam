package org.bjh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class EurekaApplicationRunner


fun main(args: Array<String>) {
    SpringApplication.run(EurekaApplicationRunner::class.java, *args)
}