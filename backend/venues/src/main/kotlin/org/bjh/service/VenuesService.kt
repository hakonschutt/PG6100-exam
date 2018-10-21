package main.kotlin.org.bjh.service

import main.kotlin.org.bjh.converter.VenueConverter
import main.kotlin.org.bjh.dto.VenuesDto
import main.kotlin.org.bjh.repository.VenuesRepository
import org.springframework.stereotype.Service

@Service
class VenuesService {
    private lateinit var venuesRepository:VenuesRepository
    fun findAll():List<VenuesDto> {
        val listOfVenueEnities = venuesRepository.findAll()

        return  VenueConverter.transform(listOfVenueEnities)

    }
    fun findById(id:Long): VenuesDto {

        return VenueConverter.transform(venuesRepository.findById(id).get())
    }
}
