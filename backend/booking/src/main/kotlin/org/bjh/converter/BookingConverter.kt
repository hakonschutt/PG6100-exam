package org.bjh.converter

import org.bjh.dto.BookingDto
import org.bjh.entity.BookingEntity

/**
 * @author hakonschutt
 */
class BookingConverter {
    companion object {

        fun transform(entity: BookingEntity): BookingDto {
            return BookingDto(
                user = entity.user,
                event = entity.event,
                tickets = entity.tickets.asSequence().map { ticketEntity ->  TicketConverter.transform(ticketEntity) }.toSet(),
                id = entity.id
            ).apply {
                id = entity.id
            }
        }

        fun transform(entities: Set<BookingEntity>): Set<BookingDto> {
            return entities.asSequence().map { transform(it) }.toSet()
        }

        fun transformDtoToEntity(entities: Set<BookingDto>) : Set<BookingEntity> {
            return entities.asSequence().map { transform(it) }.toSet()
        }

        private fun transform(entity: BookingDto) : BookingEntity {
            return BookingEntity(
                user = entity.user,
                event = entity.event,
                tickets = entity.tickets.asSequence().map { ticketDto -> TicketConverter.transformDtoToEntity(ticketDto) }.toSet()
            )
        }
    }
}