package org.bjh.workers

import org.bjh.dto.MessageDto
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

class WorkSender(val id: String) {

    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange


    fun send(list: List<MessageDto>) {
        list.forEach { send(it) }
    }

    private fun send(msg:MessageDto) {

        val exchangeName = fanout.name
        val routingKey = ""
        println("SENDING $exchangeName, $routingKey $msg")
        template.convertAndSend(exchangeName, routingKey, msg)
        println("Finished")
    }
}