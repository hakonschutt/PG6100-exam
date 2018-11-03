package org.bjh.service

import org.bjh.converter.BookingConverter
import org.bjh.dto.BookingDto
import org.bjh.repository.BookingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingService {
    @Autowired
    private lateinit var bookingRepository : BookingRepository

    fun findAll (withTickets : Boolean) : Set<BookingDto> {
        val bookings = bookingRepository.findAll().map { bookingEntity ->
            BookingConverter.transform(bookingEntity, withTickets)
        }

        return bookings.toSet()
    }

    fun findAllByEventIdAndUserId(withTickets: Boolean, eventId: Long, userId: Long) : Set<BookingDto> {
        val bookings = bookingRepository.findAllByEventIdAndUserId(eventId, userId).map { bookingEntity ->
            BookingConverter.transform(bookingEntity, withTickets)
        }

        return bookings.toSet()
    }

    fun findAllByEventId(withTickets: Boolean, eventId: Long) : Set<BookingDto> {
        val bookings = bookingRepository.findAllByEventId(eventId).map { bookingEntity ->
            BookingConverter.transform(bookingEntity, withTickets)
        }

        return bookings.toSet()
    }

    fun findAllByUserId(withTickets: Boolean, userId: Long) : Set<BookingDto> {
        val bookings = bookingRepository.findAllByUserId(userId).map { bookingEntity ->
            BookingConverter.transform(bookingEntity, withTickets)
        }

        return bookings.toSet()
    }

}