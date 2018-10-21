package main.kotlin.org.bjh.converter

import main.kotlin.org.bjh.dto.VenuesDto
import main.kotlin.org.bjh.entity.VenuesEntity

class VenueConverter {
    companion object {

        fun transform(entity: VenuesEntity): VenuesDto {
            return VenuesDto(
                    id = entity.id?.toString(),
                    name = entity.name,
                    geoLocation = entity.geoLocation,
                    address = entity.address,
                    rooms = entity.rooms
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Iterable<VenuesEntity>): List<VenuesDto> {
            return entities.map { transform(it) }
        }
    }

}


