package main.kotlin.org.bjh.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import main.kotlin.org.bjh.converter.VenueConverter
import main.kotlin.org.bjh.dto.VenueDto
import main.kotlin.org.bjh.entity.VenueEntity
import main.kotlin.org.bjh.repository.VenuesRepository
import main.kotlin.org.bjh.service.VenuesService
import org.bjh.dto.MultipleVenuesResponseDto
import org.bjh.dto.VenueResponseDto
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_VENUES_JSON = "application/org.bjh.dto.VenueResponseDto;charset=UTF-8;version=2"
@RestController
@RequestMapping("/venues")
@Api
class VenuesApi {
    private lateinit var venuesService: VenuesService

    @GetMapping(produces =[(MediaType.APPLICATION_JSON_VALUE)])
    fun getAllVenues(): ResponseEntity<WrappedResponse<List<VenueDto>>> {

       val resultList = venuesService.findAll()
        //todo add pagination
        val wrappedResponse = MultipleVenuesResponseDto(code = 200,data=resultList).validated()

        return ResponseEntity.status(200).body(wrappedResponse)
    }

    @GetMapping(path= (arrayOf("/{id}")), produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getVenue( @ApiParam("The unique id of the venue")
                  @PathVariable("id" ) idFromPath:String): ResponseEntity<WrappedResponse<VenueDto>> {
        val result:ResponseEntity<WrappedResponse<VenueDto>>
        val id:Long

        try{
            id = idFromPath.toLong()
        }catch (e : Exception){
            return ResponseEntity.status(404).build()
        }

       val venue = venuesService.findById(id)
       result = if(venue.id != null){
             ResponseEntity.status(200).body(VenueResponseDto(code = 200, data = venue).validated())
        }else{
            ResponseEntity.status(404).build()
       }
        return result

    }
     @PostMapping(consumes = [V2_VENUES_JSON, BASE_JSON])
     @ApiResponse(code = 201, message = "The id of newly created venue")
     fun createVenue(
             @ApiParam("Text of address, geoloacation, List of room ids. Should not specify id")
             @RequestBody
             dto: VenueDto)
             : ResponseEntity<Long> {
         //todo change this response
        val newVen =venuesService
         return ResponseEntity.status(201).build();
     }




}