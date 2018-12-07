package org.bjh;

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





    /*
        Here using a Direct exchange, but
        not the default one
     */
    @Bean
    fun direct(): DirectExchange {
        return DirectExchange("tut.direct")
    }

    /*
        Creating 2 different queues, X and Y
     */

    @Bean
    fun queueX(): Queue {
        return AnonymousQueue()
    }
    @Bean
    fun queueY(): Queue {
        return AnonymousQueue()
    }



    @Bean
    fun bindingX_ERROR(direct: DirectExchange,
                       queueX: Queue): Binding {
        return BindingBuilder
                .bind(queueX)
                .to(direct)
                .with("ERROR")
    }

    @Bean
    fun bindingY_ERROR(direct: DirectExchange,
                       queueY: Queue): Binding {
        return BindingBuilder
                .bind(queueY)
                .to(direct)
                .with("ERROR")
    }


    @Bean
    fun bindingY_WARN(direct: DirectExchange,
                      queueY: Queue): Binding {
        return BindingBuilder
                .bind(queueY)
                .to(direct)
                .with("WARN")
    }

}

fun main(args: Array<String>) {
    runApplication<ChatRunner>(*args)

}
