package org.bjh.converter

import org.bjh.dto.TicketDto
import org.bjh.entity.TicketEntity

/**
 * @author hakonschutt
 */
class TicketConverter {
    companion object {

        fun transform(entity: TicketEntity): TicketDto {
            return TicketDto(
                seat = entity.seat,
                price = entity.price,
                id = entity.id
            ).apply {
                id = entity.id
            }
        }

        fun transform(entities: Set<TicketEntity>): Set<TicketDto> {
            return entities.asSequence().map { transform(it) }.toSet()
        }

        fun transformDtosToEntities(entities: Set<TicketDto>) : Set<TicketEntity> {
            return entities.asSequence().map { transformDtoToEntity(it) }.toSet()
        }

        fun transformDtoToEntity(entity: TicketDto) : TicketEntity {
            return TicketEntity(
                seat = entity.seat,
                price = entity.price
            )
        }
    }
}
