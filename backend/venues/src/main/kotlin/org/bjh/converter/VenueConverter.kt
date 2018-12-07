package org.bjh.converter

import org.bjh.dto.VenueDto
import org.bjh.entity.VenueEntity

class VenueConverter {
    companion object {

        fun transform(entity: VenueEntity): VenueDto {
            return VenueDto(
                    id = entity.id?.toString(),
                    name = entity.name,
                    geoLocation = entity.geoLocation,
                    address = entity.address,
                    rooms = entity.rooms.asSequence().map { roomEntity ->  RoomConverter.transform(roomEntity) }.toSet()
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Iterable<VenueEntity>): List<VenueDto> {
            return entities.map { transform(it) }
        }
    }

}


