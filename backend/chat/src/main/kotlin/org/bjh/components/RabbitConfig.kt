package org.bjh.components

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration



@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("org.bjh.chat")
    }
}