package org.bjh.workers

import org.bjh.dto.MessageDto
import org.bjh.service.MsgService
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RabbitListener(queues = ["MessageQueue"])
class WorkReceiver(val id: String) {

    @Autowired
    private lateinit var msgService: MsgService

    private var latch: CountDownLatch = CountDownLatch(0)

    @RabbitHandler
    fun receive(msg: MessageDto): List<MessageDto> {

        if (!(msg.toUser.isNullOrEmpty() && msg.fromUser.isNullOrEmpty())) {
            try {

                val fromUserLong = msg.fromUser?.toLong()
                val toUserLong = msg.toUser?.toLong()
                return fetchMessages(fromUser = fromUserLong!!, toUser = toUserLong!!)
            } catch (e: NumberFormatException) {
                println("Number format issue")
            }
        }

        return listOf()
    }

    private fun fetchMessages(fromUser: Long, toUser: Long): List<MessageDto> {
        return msgService.fetch(fromUser, toUser)

    }
//    fun await(seconds: Int): Boolean {
//        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
//    }
}