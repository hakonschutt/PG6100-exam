package org.bjh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class GatewayApplication


fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}