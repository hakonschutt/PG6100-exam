package org.bjh.converter

import org.bjh.dto.MessageDto
import org.bjh.entity.Message

class MessageConverter{
    companion object {
        fun toDto(msg: Message): MessageDto {
            return MessageDto(
                    id = msg.id?.toString(),
                    fromUser = msg.fromUser?.toString(),
                    toUser = msg.toUser?.toString()
            ).apply {
                id = msg.id?.toString()
            }
        }
    }
}