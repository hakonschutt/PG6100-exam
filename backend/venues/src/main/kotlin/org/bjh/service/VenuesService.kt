package org.bjh.service

import org.bjh.converter.VenueConverter
import org.bjh.dto.VenueDto
import org.bjh.entity.VenueEntity

import org.bjh.repository.VenuesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VenuesService {
    @Autowired
    private lateinit var venuesRepository: VenuesRepository

    fun createVenue(dto: VenueDto): Long {

        val venueEntity = venuesRepository.save(VenueEntity(id = null, name = dto.name, geoLocation = dto.geoLocation, address = dto.address))

        return venueEntity.id ?: -1
    }

    fun updateVenue(dto: VenueDto): Long {
        val id: Long
        try {
            id = dto.id!!.toLong()
        } catch (e: Exception) {
            return -1
        }
        val venueEntity = venuesRepository.save(VenueEntity(id = id, name = dto.name, geoLocation = dto.geoLocation, address = dto.address))
        return venueEntity.id ?: -1
    }

    fun findAll(): List<VenueDto> {

        val listOfVenueEntities = venuesRepository.findAll()
        return VenueConverter.transform(listOfVenueEntities)

    }

    fun findById(id: Long): VenueDto {
        val dto = venuesRepository.findById(id)

        return if (dto.isPresent) {
            VenueConverter.transform(dto.get())
        } else {
            VenueDto(id = null, geoLocation = null, address = null, rooms = setOf(), name = null)
        }
    }

    fun delete(id: Long): Long {

        val deleted: Long? = (venuesRepository.findById(id).get()).id
        return deleted ?: -1L

    }

}
