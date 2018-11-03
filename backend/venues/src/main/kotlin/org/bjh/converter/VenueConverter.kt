package org.bjh.converter

import org.bjh.dto.VenueDto
import org.bjh.entity.VenueEntity
import org.bjh.pagination.PageDto
import org.springframework.data.domain.Page
import kotlin.streams.toList

class VenueConverter {
    companion object {
        fun transform(entity: VenueEntity, withRooms: Boolean=false): VenueDto {
            return VenueDto(
                    id = entity.id?.toString(),
                    name = entity.name,
                    geoLocation = entity.geoLocation,
                    address = entity.address,
                    rooms = when {
                        withRooms -> entity.rooms.asSequence().map { roomEntity ->
                            RoomConverter.transform(roomEntity)
                        }.toSet()
                        else -> setOf()
                    }
            ).apply {
                id = entity.id?.toString()
            }
        }
        fun transform(venueList: List<VenueEntity>,
                      withRooms: Boolean,offset:Int= 0,limit:Int = 20)
                : PageDto<VenueDto> {
                val venueDto = venueList.map{ transform(it,withRooms)}
//            val dtoList: MutableList<NewsDto> = newsList.stream()
//                    .skip(offset.toLong()) // this is a good example of how streams simplify coding
//                    .limit(limit.toLong())
//                    .map { transform(it, withComments, withVotes) }
//                    .toList().toMutableList()
            return PageDto(
                    list = venueDto,
                    rangeMin = offset,
                    rangeMax = offset + venueList.size - 1,
                    totalSize = venueList.size
            )
        }
    }
}


