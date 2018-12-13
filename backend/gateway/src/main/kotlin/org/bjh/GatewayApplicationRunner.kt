package org.bjh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

<<<<<<< HEAD
@EnableEurekaClient
=======
>>>>>>> 9ef48d48059027e7ed4467b0a6ff940fb00d0dd9
@SpringBootApplication
@EnableDiscoveryClient
class GatewayApplication


fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}