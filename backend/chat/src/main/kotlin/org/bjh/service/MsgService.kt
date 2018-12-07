package org.bjh.service

import org.bjh.converter.MessageConverter
import org.bjh.dto.MessageDto
import org.bjh.entity.Message
import org.bjh.repository.MsgRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MsgService{
    @Autowired
    private lateinit var msgRepository: MsgRepository
    fun save(msgDto: MessageDto): Boolean {
        try {
            val fromUserLong= msgDto.fromUser!!.toLong()
            val toUserLong= msgDto.toUser!!.toLong()
            val msgEntity = Message(msg = msgDto.msgs, fromUser = fromUserLong , toUser = toUserLong)
            msgRepository.save(msgEntity)
            return true
        }catch (e:NumberFormatException){
            println("Wrong format")
            return false
        }


    }
    fun fetch(toUser: Long,fromUser: Long): List<MessageDto> {
        val listOfMessages = msgRepository.findAllByFromUserAndToUser(fromUser,toUser)
        return listOfMessages.map { MessageConverter.toDto(it) }
    }
}