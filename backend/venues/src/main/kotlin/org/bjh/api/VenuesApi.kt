package org.bjh.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.*
import org.bjh.dto.VenueDto
import org.bjh.service.VenuesService
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueResponseDto
import org.bjh.pagination.PageDto
import org.bjh.service.RoomService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.NumberFormatException

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_VENUES_JSON = "application/org.bjh.dto.VenueDto;charset=UTF-8"
const val HAL_V1 = "application/hal+json;charset=UTF-8;version=1"

@RestController
@RequestMapping("/api/venues")
@Api("Api for venues")

class VenuesApi {
    @Autowired
    private lateinit var venuesService: VenuesService
    @Autowired
    private lateinit var roomService: RoomService

    //todo set up query params for withrooms or nawt
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiResponse(code = 200, message = "Returns a list of venues")
    fun getAllVenues(

            @ApiParam("Loading with rooms, or not, default is without")
            @RequestParam("withRooms", required = false, defaultValue = "false")
            withRooms: Boolean,

            @ApiParam("Offset param to determine what part ofthe  result table you want back")
            @RequestParam("offset", required = false, defaultValue = "0")
            offset: Int,
            @ApiParam("Limit param to determine what size of the result table you want back")
            @RequestParam("limit", required = false, defaultValue = "20")
            limit: Int


    ): ResponseEntity<WrappedResponse<PageDto<VenueDto>>> {

        val resultList = venuesService.findAll(withRooms, offset, limit)
        val wrappedResponse = VenueResponseDto(code = 200, data = resultList, message = "list of venues").validated()

        return ResponseEntity.status(200).body(wrappedResponse)
    }

    //todo set up query params for withrooms or nawt
    @GetMapping(path = ["/{id}"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getVenue(@ApiParam("The unique id of the venue")
                 @PathVariable("id") idFromPath: String,
                 @ApiParam("loading with rooms, or not, default is with")
                 @RequestParam("withRooms", required = false)
                 withRooms: Boolean = true
    ): ResponseEntity<WrappedResponse<PageDto<VenueDto>>> {
        val result: ResponseEntity<WrappedResponse<PageDto<VenueDto>>>
        val id: Long

        try {
            id = idFromPath.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(404).build()
        }

        val venuePage = venuesService.findAllById(id, withRooms)
        result = if (venuePage.list.size > 0) {
            ResponseEntity.status(200)
                    .body(VenueResponseDto(
                            code = 200,
                            data = venuePage,
                            message = "The single venue that was requested")
                            .validated())
        } else {
            ResponseEntity.status(404).build()
        }
        return result

    }

    @PostMapping(consumes = [V2_VENUES_JSON, BASE_JSON])
    @ApiResponses(
            //FIXME : Return url instead of id
            ApiResponse(code = 201, message = "The id of newly created venue"),
            ApiResponse(code = 400, message = "Bad request")
    )
    fun createVenue(
            @ApiParam("Text of address, geoloacation, List of room ids. Should not specify id")
            @RequestBody
            dto: VenueDto)
            : ResponseEntity<Long> {
        //todo change this response

        if (!dto.id.isNullOrEmpty() && dto.rooms.isEmpty() &&
                !(dto.address == null || dto.geoLocation == null)) {
            //There should atleast be one room, an address, no id, and a geolocation to make it possible to create a venue

            return ResponseEntity.status(400).build()
        }
        val checkIfRoomHasValidFieldValues: (RoomDto) -> Boolean = {
            (!it.id.isNullOrBlank()) ||
                    !it.name.isNullOrBlank() ||
                    (it.columns != null) ||
                    (it.rows != null)
        }
        // should this return 400 if one or more of multiple rooms has errors?
        val rooms = dto.rooms
                .asSequence()
                .filter(checkIfRoomHasValidFieldValues).toSet()

        if (rooms.isEmpty()) {
            return ResponseEntity.status(400).build()
        }


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
        println("deletedVenue ${deletedVenueId}")

        return if (deletedVenueId > -1) {
            ResponseEntity.status(204).build()
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
        val dto = venuesService.findAllById(id = id, withRooms = true)
        //returns responseEntity if service did not find an already created entity
        if (dto.list.isEmpty()) return ResponseEntity.status(404).build()
        val jackson = ObjectMapper()

        val jsonNode: JsonNode
        val dtoObj = dto.list[0]
        try {

            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }
        if (jsonNode.has("id")) return ResponseEntity.status(409).build()

        var newName = dtoObj.name
        var newAddress = dtoObj.address
        var newGeo = dtoObj.geoLocation



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
        dtoObj.name = newName
        dtoObj.address = newAddress
        dtoObj.geoLocation = newGeo

        val response = venuesService.updateVenue(dtoObj)

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
        val venueDtoList = venuesService.findAllById(id = venueId, withRooms = true)
        if (venueDtoList.list.isEmpty()) return ResponseEntity.status(404).build()
        val venueDto = venueDtoList.list[0]
        val isRoomInVenue = venueDto.rooms.any { it.id == roomIdFromPath }

        if (!isRoomInVenue) return ResponseEntity.status(404).build()

        val roomDto = roomService.findById(roomId)

        if ((venueDto.id.isNullOrBlank() || roomDto.id.isNullOrBlank())) return ResponseEntity.status(404).build()

        val jackson = ObjectMapper()
        println("JsonPatch content : $jsonPatch")
        val jsonNode: JsonNode
        try {

            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        println("JsonNode : $jsonNode")
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

        val room = roomService.save(roomDto)


        return when {
            room.id == null -> ResponseEntity.status(500).build()
            else -> ResponseEntity.status(204).build()
        }
    }

    @ApiOperation("Update a specific venue")
    @PutMapping(path = ["/{id}"])
    fun updateById(
            @ApiParam("The id of the venue")
            @PathVariable("id")
            pathId: String,
            @ApiParam("New data for updating the venue")
            @RequestBody
            dto: VenueDto
    ): ResponseEntity<WrappedResponse<Void>> {
        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).body(WrappedResponse<Void>(code = 400, message = " Invalid id $pathId").validated())
        }
        if (dto.id == null) {
            return ResponseEntity.status(400).body(WrappedResponse<Void>(code = 400, message = " Dto is missing id").validated())
        }
        if (dto.id != pathId) {
            return ResponseEntity.status(409).body(WrappedResponse<Void>(code = 409, message = " Inconsistent id between URL and JSON payload").validated())
        }

        val dtoList = venuesService.findAllById(id,true).list
        if(dtoList.isEmpty()){
            return ResponseEntity.status(404).body(WrappedResponse<Void>(code = 404, message = " Dto is missing id").validated())
        }
        val dtoToSave = dtoList[0]
        dtoToSave.name = dto.name!!
        dtoToSave.rooms = dto.rooms!!
        dtoToSave.address = dto.address!!
        dtoToSave.geoLocation = dto.geoLocation!!


        venuesService.updateVenue(dtoToSave)
        return ResponseEntity.status(204).body(
                WrappedResponse<Void>(code = 204).validated())
    }

}
