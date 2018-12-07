package org.bjh.repository

import org.bjh.entity.Message
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface MsgRepository : PagingAndSortingRepository<Message,Long> {
     fun findAllByFromUserAndToUser(fromUser: Long, toUser: Long): List<Message>
}