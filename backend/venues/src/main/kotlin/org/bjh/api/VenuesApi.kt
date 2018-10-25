package org.bjh.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.bjh.dto.VenueDto
import org.bjh.service.VenuesService
import org.bjh.dto.MultipleVenuesResponseDto
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueResponseDto
import org.bjh.service.RoomService
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.function.Predicate

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_VENUES_JSON = "application/org.bjh.dto.VenueResponseDto;charset=UTF-8;version=2"

@RestController
@RequestMapping("/venues")
@Api("Api for venues")
class VenuesApi {
    private lateinit var venuesService: VenuesService
    private lateinit var roomService: RoomService

    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiResponse(code = 200, message = "Returns a list of venues")
    fun getAllVenues(): ResponseEntity<WrappedResponse<List<VenueDto>>> {

        val resultList = venuesService.findAll()
        //todo add pagination
        val wrappedResponse = MultipleVenuesResponseDto(code = 200, data = resultList).validated()

        return ResponseEntity.status(200).body(wrappedResponse)
    }

    @GetMapping(path = (arrayOf("/{id}")), produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getVenue(@ApiParam("The unique id of the venue")
                 @PathVariable("id") idFromPath: String): ResponseEntity<WrappedResponse<VenueDto>> {
        val result: ResponseEntity<WrappedResponse<VenueDto>>
        val id: Long

        try {
            id = idFromPath.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val venue = venuesService.findById(id)
        result = if (venue.id != null) {
            ResponseEntity.status(200).body(VenueResponseDto(code = 200, data = venue).validated())
        } else {
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

        if (!(dto.id.isNullOrEmpty() && !dto.rooms.isEmpty()) || (dto.address == null || dto.geoLocation == null)) {
            //There should atleast be one room, an address, no id, and a geolocation to make it possible to create a venue
            return ResponseEntity.status(400).build()
        }
        val checkIfRoomHasValidFieldValues: (RoomDto) -> Boolean = {
            (!it.id.isNullOrBlank()) && !it.name.isNullOrBlank() && (it.columns != null && it.columns > 0) && (it.rows != null && it.rows > 0)
        }
        // should this return 400 if one or more of multiple rooms has errors?
        val rooms = dto.rooms
                .filter(checkIfRoomHasValidFieldValues).toSet()

        if (rooms.isEmpty()) return ResponseEntity.status(400).build()

        val roomsSaved = roomService.saveAll(rooms)

        val venueId = venuesService.createVenue(dto)

        return ResponseEntity.status(201).body(venueId)
    }

    @DeleteMapping(path = ["/{id}"])
    @ApiResponse(code = 200, message = "Deletes the venue with corresponding Id")
    fun deleteVenue(@ApiParam("The unique id of the venue to delete")
                    @PathVariable("id") idFromPath: String): ResponseStatus {
        return venuesService.delete(idFromPath!!.toLong())


    }


}