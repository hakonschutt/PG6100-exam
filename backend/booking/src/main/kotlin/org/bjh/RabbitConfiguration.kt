package org.bjh

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Source: https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html
 * Altered by hakonschutt
 */
@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("org.bjh.direct.payment")
    }

    @Bean
    fun queue(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun binding(fanout: FanoutExchange,
                queue: Queue): Binding {
        return BindingBuilder.bind(queue).to(fanout)
    }
}