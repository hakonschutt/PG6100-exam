package org.bjh.service

import org.bjh.converter.BookingConverter
import org.bjh.converter.TicketConverter
import org.bjh.dto.BookingDto
import org.bjh.entity.BookingEntity
import org.bjh.pagination.HalLink
import org.bjh.pagination.PageDto
import org.bjh.repository.BookingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class BookingService {
    @Autowired
    private lateinit var bookingRepository : BookingRepository

    @Cacheable("bookingCache")
    fun findAll (withTickets : Boolean, offset: Int = 0, limit: Int = 20) : PageDto<BookingDto> {
        val bookings = bookingRepository.findAll(offset, limit)
        val page = BookingConverter.transform(bookings, withTickets, offset, limit)

        val builder = UriComponentsBuilder
            .fromPath("/bookings")
            .queryParam("withTickets", withTickets)
            .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
            .queryParam("offset", offset)
            .build().toString()
        )

        if (!bookings.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < bookingRepository.count()) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }

        return page
    }

    fun findAllByEventIdAndUserId(withTickets: Boolean, eventId: Long, userId: Long, offset: Int = 0, limit: Int = 20) : PageDto<BookingDto> {
        val bookings = bookingRepository.findAllByEventIdAndUserId(eventId, userId, offset, limit)
        val page = BookingConverter.transform(bookings, withTickets, offset, limit)

        val builder = UriComponentsBuilder
                .fromPath("/bookings")
                .queryParam("withTickets", withTickets)
                .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )

        if (!bookings.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < bookingRepository.countByEventIdAndUserId(eventId, userId)) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }

        return page
    }

    fun findAllByEventId(withTickets: Boolean, eventId: Long, offset: Int = 0, limit: Int = 20) : PageDto<BookingDto> {
        val bookings = bookingRepository.findAllByEventId(eventId, offset, limit)
        val page = BookingConverter.transform(bookings, withTickets, offset, limit)

        val builder = UriComponentsBuilder
                .fromPath("/bookings")
                .queryParam("withTickets", withTickets)
                .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )

        if (!bookings.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < bookingRepository.countByEventId(eventId)) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }

        return page
    }

    fun findAllByUserId(withTickets: Boolean, userId: Long, offset: Int = 0, limit: Int = 20) : PageDto<BookingDto> {
        val bookings = bookingRepository.findAllByUserId(userId, offset, limit)
        val page = BookingConverter.transform(bookings, withTickets, offset, limit)

        val builder = UriComponentsBuilder
                .fromPath("/bookings")
                .queryParam("withTickets", withTickets)
                .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )

        if (!bookings.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < bookingRepository.countByUserId(userId)) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }

        return page
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

    fun updateBooking(dto: BookingDto) : Boolean {
        val id: Long
        try {
            id = dto.id!!.toLong()
        } catch (e: Exception) {
            return false
        }

        val bookingEntity = bookingRepository.save(
            BookingEntity(id = id, user = dto.user, event = dto.event, tickets = TicketConverter.transformDtosToEntities(dto.tickets))
        )

        return bookingEntity.id != null
    }

    fun deleteBookingById(id: Long) : Boolean {
        if (!bookingRepository.existsById(id)) {
            return false
        }

        bookingRepository.deleteById(id)

        return true
    }

}