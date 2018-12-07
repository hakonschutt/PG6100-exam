package org.bjh.workers

import org.bjh.dto.MessageDto
import org.bjh.service.MsgService
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
//
//@RabbitListener(queues = ["MessageQueue"],autoStartup = "false")
//class WorkReceiver(val id: String) {
//
//    @Autowired
//    private lateinit var msgService: MsgService
//
//    private var latch: CountDownLatch = CountDownLatch(0)
//
//    @RabbitHandler
//    fun receive(msg: MessageDto): List<MessageDto> {
//
//        if (!(msg.toUser.isNullOrEmpty() && msg.fromUser.isNullOrEmpty())) {
//            try {
//
//                val fromUserLong = msg.fromUser?.toLong()
//                val toUserLong = msg.toUser?.toLong()
//                return fetchMessages(fromUser = fromUserLong!!, toUser = toUserLong!!)
//            } catch (e: NumberFormatException) {
//                println("Number format issue")
//            }
//        }
//
//        return listOf()
//    }
//
//     fun fetchMessages(fromUser: Long, toUser: Long): List<MessageDto> {
//        return msgService.fetch(fromUser, toUser)
//
//    }
//    fun await(seconds: Int): Boolean {
//        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
//    }
@Service
class Receiver {

    @Autowired
    private lateinit var messages: ReceivedMessages

    /*
        Here I am defining two different handlers, listening
        for messages from 2 different queues.
     */


    //as anonymous queues have random names, we need to resolve them at runtime
    @RabbitListener(queues = ["#{queueX.name}"])
    fun receiverX(msg: String) {
        doWork("X", msg)
    }

    @RabbitListener(queues = ["#{queueY.name}"])
    fun receiverY(msg: String) {
        doWork("Y", msg)
    }

    private fun doWork(receiver: String, msg: String){

        val s = "$receiver received: '$msg'"

        messages.addMessage(s)
    }
}