package org.bjh.service

import org.bjh.converter.BookingConverter
import org.bjh.converter.TicketConverter
import org.bjh.dto.BookingDto
import org.bjh.entity.BookingEntity
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

    fun findById(id: Long, withTickets: Boolean) : BookingDto {
        val booking = bookingRepository.findById(id)

        return when {
            booking.isPresent -> BookingConverter.transform(booking.get(), withTickets)
            else ->
                return BookingDto(id = null, user = null, event = null, tickets = setOf())
        }
    }

    fun createBooking(dto: BookingDto) : Long {
        val booking = bookingRepository.save(
            BookingEntity(
                id = null,
                user = dto.user!!,
                event = dto.event!!,
                tickets = TicketConverter.transformDtosToEntities(dto.tickets)
            )
        )

        return booking.id ?: 1L
    }

    fun deleteBookingById(id: Long) : Boolean {
        if (!bookingRepository.existsById(id)) {
            return false
        }

        bookingRepository.deleteById(id)

        return true
    }

}