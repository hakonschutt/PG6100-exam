package org.bjh.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.bjh.dto.VenueDto
import org.bjh.service.VenuesService
import org.bjh.dto.MultipleVenuesResponseDto
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueResponseDto
import org.bjh.service.RoomService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_VENUES_JSON = "application/org.bjh.dto.VenueDto;charset=UTF-8"

@RestController
@RequestMapping("/api/venues")
@Api("Api for venues")
class VenuesApi {
    @Autowired
    private lateinit var venuesService: VenuesService
    @Autowired
    private lateinit var roomService: RoomService

    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiResponse(code = 200, message = "Returns a list of venues")
    fun getAllVenues(): ResponseEntity<WrappedResponse<List<VenueDto>>> {

        val resultList = venuesService.findAll()
        //todo add pagination
        val wrappedResponse = MultipleVenuesResponseDto(code = 200, data = resultList, message = "list of venues").validated()

        return ResponseEntity.status(200).body(wrappedResponse)
    }

    @GetMapping(path = ["/{id}"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
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
            ResponseEntity.status(200).body(VenueResponseDto(code = 200, data = venue, message = "The single venue that was requested").validated())
        } else {
            ResponseEntity.status(404).build()
        }
        return result

    }

    @PostMapping( consumes = [V2_VENUES_JSON, BASE_JSON])
    @ApiResponse(code = 201, message = "The id of newly created venue")
    fun createVenue(
            @ApiParam("Text of address, geoloacation, List of room ids. Should not specify id")
            @RequestBody
            dto: VenueDto)
            : ResponseEntity<Long> {
        //todo change this response
        println("CREATING A ")
        if (!dto.id.isNullOrEmpty() && dto.rooms.isEmpty() && !(dto.address == null || dto.geoLocation == null)) {
            //There should atleast be one room, an address, no id, and a geolocation to make it possible to create a venue
            println("Returning 400")
            return ResponseEntity.status(400).build()
        }
        val checkIfRoomHasValidFieldValues: (RoomDto) -> Boolean = {
            (!it.id.isNullOrBlank()) || !it.name.isNullOrBlank()
                    || (it.columns != null) || (it.rows != null)
        }
        // should this return 400 if one or more of multiple rooms has errors?
        val rooms = dto.rooms
                .filter(checkIfRoomHasValidFieldValues).toSet()

        if (rooms.isEmpty()) {
            println("******* ROOMS ARE EMPTY OR SOMETHING *********")
            return ResponseEntity.status(400).build()
        }

       // val roomsSaved = roomService.saveAll(rooms)

        val venueId = venuesService.createVenue(dto)

        return ResponseEntity.status(201).body(venueId)
    }

    @DeleteMapping(path = ["/{id}"])
    @ApiResponses(
            ApiResponse(code = 200, message = "Deletes the venue with corresponding Id"),
            ApiResponse(code = 404, message = "Resource not found")
    )
    fun deleteVenue(@ApiParam("The unique id of the venue to delete")
                    @PathVariable("id") idFromPath: String): ResponseEntity<Unit> {

        val id: Long
        try {
            id = idFromPath.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }
        val deletedVenueId = venuesService.delete(id)

        //todo figure out if this id should be in a wrapped response

        return if (deletedVenueId > -1) {
            ResponseEntity.status(200).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping(path = ["/{id}"], consumes = ["application/merge-patch+json"])
    @ApiResponses(
            ApiResponse(code = 409, message = "Conflict"),
            ApiResponse(code = 400, message = "Bad request due to invalid syntax."),
            ApiResponse(code = 204, message = "The server has successfully fulfilled the request"),
            ApiResponse(code = 500, message = "Server is expecting unpredictable")
    )
    fun mergePatchVenue(@ApiParam("The unique id of the venue")
                        @PathVariable("id")
                        idFromPath: String,
                        @ApiParam("The partial patch")
                        @RequestBody
                        jsonPatch: String)
            : ResponseEntity<Unit> {
        val id: Long
        try {
            id = idFromPath.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }
        val dto = venuesService.findById(id)
        //returns responseEntity if service did not find an already created entity
        dto.id ?: return ResponseEntity.status(404).build()
        val jackson = ObjectMapper()

        val jsonNode: JsonNode
        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }
        if (jsonNode.has("id")) return ResponseEntity.status(409).build()

        var newName = dto.name
        var newAddress = dto.address
        var newGeo = dto.geoLocation



        if (jsonNode.has("name")) {
            val nameNode = jsonNode.get("name")
            newName = when {
                nameNode.isNull -> null
                nameNode.isTextual -> nameNode.asText()
                else -> return ResponseEntity.status(400).build()
            }
        }
        if (jsonNode.has("address")) {

            val valueNode = jsonNode.get("address")

            newAddress = when {
                valueNode.isNull -> null
                valueNode.isTextual -> valueNode.asText()
                else -> return ResponseEntity.status(400).build()
            }
        }
        if (jsonNode.has("geo")) {

            val valueNode = jsonNode.get("geo")
            newGeo = when {
                valueNode.isNull -> null
                valueNode.isTextual -> valueNode.asText()
                else ->
                    return ResponseEntity.status(400).build()
            }
        }
        dto.name = newName
        dto.address = newAddress
        dto.geoLocation = newGeo

        val response = venuesService.updateVenue(dto)

        return if (response < 0) {
            ResponseEntity.status(500).build()
        } else {
            ResponseEntity.status(204).build()
        }
    }


    @PatchMapping(path = ["/{venue_id}/rooms/{room_id}"], consumes = ["application/merge-patch+json"])
    @ApiResponses(
            ApiResponse(code = 409, message = "Conflict"),
            ApiResponse(code = 400, message = "Bad request due to invalid syntax."),
            ApiResponse(code = 204, message = "The server has successfully fulfilled the request")
    )
    fun mergePatchRoom(@ApiParam("The unique id of the room")
                       @PathVariable("venue_id")
                       venueIdFromPath: String,
                       @PathVariable("room_id")
                       roomIdFromPath: String,
                       @ApiParam("The partial patch")
                       @RequestBody
                       jsonPatch: String)
            : ResponseEntity<Unit> {
        val venueId: Long
        val roomId: Long
        try {
            venueId = venueIdFromPath.toLong()
            roomId = roomIdFromPath.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }
        val venueDto = venuesService.findById(venueId)

        val isRoomInVenue = venueDto.rooms.any { it.id == roomIdFromPath }

        if (!isRoomInVenue) return ResponseEntity.status(404).build()

        val roomDto = roomService.findById(roomId)

        if ((venueDto.id.isNullOrBlank() || roomDto.id.isNullOrBlank())) return ResponseEntity.status(404).build()

        val jackson = ObjectMapper()

        val jsonNode: JsonNode
        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }
        if (jsonNode.has("id")) return ResponseEntity.status(409).build()

        var name = roomDto.name
        var cols = roomDto.columns
        var rows = roomDto.rows

        if (jsonNode.has("name")) {
            val nameNode = jsonNode.get("name")
            name = when {
                nameNode.isNull -> null
                nameNode.isTextual -> nameNode.asText()
                else -> return ResponseEntity.status(400).build()
            }
        }
        if (jsonNode.has("columns")) {
            val colNode = jsonNode.get("columns")
            cols = when {
                colNode.isNull -> null
                colNode.isInt -> colNode.intValue()
                else -> return ResponseEntity.status(400).build()
            }
        }
        if (jsonNode.has("rows")) {
            val rowsNode = jsonNode.get("rows")
            rows = when {
                rowsNode.isNull -> null
                rowsNode.isInt -> rowsNode.intValue()
                else -> return ResponseEntity.status(400).build()
            }
        }
        roomDto.name = name
        roomDto.columns = cols
        roomDto.rows = rows

        roomService.save(roomDto)

        return if (roomService.save(roomDto).id != null) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(500).build()
        }
    }

}
