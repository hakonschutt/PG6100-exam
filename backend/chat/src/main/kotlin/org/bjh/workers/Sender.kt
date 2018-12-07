package org.bjh.workers

import org.bjh.dto.MessageDto
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class Sender {

    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var direct: DirectExchange

    private fun send(msg: String, key: String) {
        template.convertAndSend(direct.name, key, msg);
    }

    fun info(msg: String) = send("INFO: $msg", "INFO")

    fun warn(msg: String) = send("WARN: $msg", "WARN")

    fun error(msg: String) = send("ERROR: $msg", "ERROR")
}