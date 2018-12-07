package org.bjh.repository

import org.bjh.entity.BookingEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<BookingEntity, Long> {
    fun findAllByEventId(eventId: Long): Set<BookingEntity>

    fun findAllByUserId(userId: Long): Set<BookingEntity>

    fun findAllByEventIdAndUserId(eventId: Long, userId: Long): Set<BookingEntity>
}