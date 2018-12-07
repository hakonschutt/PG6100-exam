package org.bjh;

import org.bjh.workers.WorkReceiver
import org.bjh.workers.WorkSender
import org.bjh.pojo.QueueNameHolder
import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.concurrent.atomic.AtomicBoolean
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.rabbit.connection.ConnectionFactory


@SpringBootApplication
@EnableSwagger2
@EnableScheduling

class ChatRunner {
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API for venues")
                .description("REST API containing all venues for the House of Movies")
                .version("1.0")
                .build()
    }

    @Bean
    fun queue(): Queue {
        return Queue("MessageQueue")
    }

//    @Bean//https://stackoverflow.com/questions/50473562/spring-bus-rabbit-amqp-client-drops-connection-on-idle-for-long-time
//    fun amqpAdmin(connectionFactory: ConnectionFactory): AmqpAdmin {
//        return object : RabbitAdmin(connectionFactory) {
//
//            override fun initialize() {
//                while (true) { // might want to give up after some number of tries
//                    try {
//                        super.initialize()
//                        break
//                    } catch (e: Exception) {
//                        println("Failed to declare elements: " + (e.cause?.cause?.message ?: "what"))
//                        try {
//                            Thread.sleep(1000)
//                        } catch (e1: InterruptedException) {
//                            Thread.currentThread().interrupt()
//                        }
//
//                    }
//
//                }
//            }
//
//        }
//    }

    @Bean
    fun worker0(): WorkReceiver {
        return WorkReceiver("a")
    }

    @Bean
    fun sender(): WorkSender {
        return WorkSender("ok")
    }

}


fun main(args: Array<String>) {
    runApplication<ChatRunner>(*args)

}
