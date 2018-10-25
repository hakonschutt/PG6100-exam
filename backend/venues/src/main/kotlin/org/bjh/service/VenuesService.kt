package main.kotlin.org.bjh.service

import main.kotlin.org.bjh.converter.VenueConverter
import main.kotlin.org.bjh.dto.VenueDto
import main.kotlin.org.bjh.entity.VenueEntity

import main.kotlin.org.bjh.repository.VenuesRepository
import org.springframework.stereotype.Service

@Service
class VenuesService {
    private lateinit var venuesRepository:VenuesRepository
    fun createVenue(dto: VenueDto):Long{

        val venueEntity =venuesRepository.save(VenueEntity(id=null,name=dto.name,geoLocation = dto.geoLocation,address = dto.address))

        return if(venueEntity.id != null){
            venueEntity.id
        }else{
            -1
        }
    }
    fun findAll():List<VenueDto> {

        val listOfVenueEnities = venuesRepository.findAll()

        return  VenueConverter.transform(listOfVenueEnities)

    }
    fun findById(id:Long): VenueDto {
        val dto = venuesRepository.findById(id)

        return if (dto.isPresent) {
            VenueConverter.transform(dto.get())
        }else{
            VenueDto(id=null,geoLocation = null,address = null,rooms = setOf(),name = null)
        }
    }
}
