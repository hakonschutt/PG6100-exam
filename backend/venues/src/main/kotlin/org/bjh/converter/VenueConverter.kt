package main.kotlin.org.bjh.converter

import main.kotlin.org.bjh.dto.VenueDto
import main.kotlin.org.bjh.entity.VenueEntity

class VenueConverter {
    companion object {

        fun transform(entity: VenueEntity): VenueDto {
            return VenueDto(
                    id = entity.id?.toString(),
                    name = entity.name,
                    geoLocation = entity.geoLocation,
                    address = entity.address,
                    rooms = entity.rooms
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Iterable<VenueEntity>): List<VenueDto> {
            return entities.map { transform(it) }
        }
    }

}


