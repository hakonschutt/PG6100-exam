package org.bjh.service

import org.bjh.converter.RoomConverter
import org.bjh.converter.VenueConverter
import org.bjh.dto.VenueDto
import org.bjh.entity.VenueEntity
import org.bjh.pagination.HalLink
import org.bjh.pagination.PageDto

import org.bjh.repository.VenuesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder

@Service
class VenuesService {
    @Autowired
    private lateinit var venuesRepository: VenuesRepository

    fun createVenue(dto: VenueDto): Long {

    val rooms = RoomConverter.transformDtoToEntity(dto.rooms)

    val entity = VenueEntity(
            id=null,
            name = dto.name,
            geoLocation = dto.geoLocation,
            address = dto.address,
            rooms = rooms)

        val venueEntity = venuesRepository.save(entity)

        return venueEntity.id ?: -1
    }

    fun updateVenue(dto: VenueDto): Long {
        val id: Long
        try {
            id = dto.id!!.toLong()
        } catch (e: Exception) {
            return -1
        }
        val venueEntity = venuesRepository.save(VenueEntity(id = id, name = dto.name, geoLocation = dto.geoLocation, address = dto.address,
                rooms=RoomConverter.transformDtoToEntity(dto.rooms)))

        return venueEntity.id ?: -1
    }
    @Cacheable("venuesCache")
    fun findAll(withRooms: Boolean, offset: Int=0, limit: Int=20): PageDto<VenueDto> {

        val venuList = venuesRepository.findAll(offset,limit)

        val page = VenueConverter.transform(venueList =venuList ,withRooms=withRooms,offset=offset,limit=limit)


        var builder = UriComponentsBuilder
                .fromPath("/venues")
                .queryParam("withRooms", withRooms)
                .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )
//Hal link part is modified from Andreas code
        if (!venuList.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < venuesRepository.count()) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }
        return page

    }

    fun findAllById(id: Long, withRooms: Boolean): PageDto<VenueDto> {
        val dto = venuesRepository.findAllById(id)

        return VenueConverter.transform(dto,withRooms)
    }

    fun delete(id: Long): Long {

        val listOfentities = (venuesRepository.findAllById(id))
        if (listOfentities.isNotEmpty()){
            venuesRepository.delete(listOfentities[0])
        }
        return listOfentities[0].id ?: -1L

    }
}
