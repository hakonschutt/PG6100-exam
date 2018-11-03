package org.bjh.converter

import org.bjh.dto.BookingDto
import org.bjh.entity.BookingEntity

/**
 * @author hakonschutt
 */
class BookingConverter {
    companion object {
        fun transform(
            entity: BookingEntity,
            withTickets: Boolean = false
        ): BookingDto {
            return BookingDto(
                user = entity.user,
                event = entity.event,
                id = entity.id,
                tickets = when {
                    withTickets -> entity.tickets.asSequence().map { ticketEntity ->
                        TicketConverter.transform(ticketEntity)
                    }.toSet()
                    else -> setOf()
                }
            ).apply {
                id = entity.id
            }
        }

        fun transform(
            entities: Set<BookingEntity>,
            withTickets: Boolean = false,
            offset : Int = 0,
            limit: Int = 20
        ): Set<BookingDto> {
            return entities.asSequence().map { transform(it, withTickets) }.toSet()
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