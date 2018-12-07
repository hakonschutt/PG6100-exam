package org.bjh.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.*
import org.bjh.dto.BookingDto
import org.bjh.dto.TicketDto
import org.bjh.service.BookingService
import org.bjh.service.TicketService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

const val ID_PARAM = "The numeric id of the booking"
const val BASE_JSON = "application/json;charset=UTF-8"
const val MERGE_PATCH = "application/merge-patch+json"

/**
 * @author hakonschutt
 */
@RestController
@RequestMapping("/api/bookings")
@Api("Api for bookings")
class BookingApi {

    @Autowired
    private lateinit var bookingService : BookingService

    @Autowired
    private lateinit var ticketService: TicketService

    private val BASE_PATH = "/api/bookings"

    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiOperation("Get all the bookings")
    @ApiResponses(
        ApiResponse(code = 200, message = "Returns a list of all bookings"),
        ApiResponse(code = 204, message = "Returns an empty list")
    )
    fun getAllBookings(
        @ApiParam("The user id to query by")
        @RequestParam("userId", required = false)
        pathUserId: String?,

        @ApiParam("The event id to query by")
        @RequestParam("eventId", required = false)
        pathEventId: String?,

        @ApiParam("Return response with ticket objects")
        @RequestParam("withTickets", required = false, defaultValue = "false")
        withTickets: Boolean,

        @ApiParam("The offset of the response set")
        @RequestParam("offset", required = false, defaultValue = "0")
        offset: Int,

        @ApiParam("The limit of the response set")
        @RequestParam("limit", required = false, defaultValue = "20")
        limit: Int
    ): ResponseEntity<WrappedResponse<Set<BookingDto>>> {
        var userId : Long = -1L
        var eventId : Long = -1L

        try {
            if (!pathEventId.isNullOrBlank()) { eventId = pathEventId!!.toLong() }
            if (!pathUserId.isNullOrBlank()) { userId = pathUserId!!.toLong() }
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        val result = if (eventId == -1L && userId == -1L) {
            bookingService.findAll(withTickets);
        } else if (eventId != -1L && userId != -1L) {
            bookingService.findAllByEventIdAndUserId(withTickets = withTickets, eventId = eventId, userId = userId)
        } else if (eventId != -1L) {
            bookingService.findAllByEventId(withTickets, eventId = eventId)
        } else {
            bookingService.findAllByUserId(withTickets, userId = userId)
        }

        val statusCode : Int = if (result.isNotEmpty()) { 200 } else { 204 }
        val wrappedResponse = WrappedResponse(code = statusCode, data = result, message = "list of bookings").validated()

        return ResponseEntity.status(statusCode).body(wrappedResponse)
    }

    @GetMapping(path = ["/{id}"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiOperation("Get a single booking specified by id")
    @ApiResponses(
        ApiResponse(code = 200, message = "Returns a single booking"),
        ApiResponse(code = 400, message = "Booking id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found")
    )
    fun getBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String,

        @ApiParam("Return response with ticket objects")
        @RequestParam("withTickets", required = false, defaultValue = "false")
        withTickets: Boolean
    ) : ResponseEntity<WrappedResponse<BookingDto>> {
        val result: ResponseEntity<WrappedResponse<BookingDto>>
        val id: Long

        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        val booking = bookingService.findById(id, withTickets)

        result = if (booking.id != null) {
            ResponseEntity.status(200).body(
                WrappedResponse(code = 200, data = booking, message = "booking with id $pathId").validated()
            )
        } else {
            ResponseEntity.status(404).build()
        }

        return result
    }

    @PostMapping(consumes = [BASE_JSON])
    @ApiOperation("Create a new booking")
    @ApiResponses(
        ApiResponse(code = 201, message = "Booking was created"),
        ApiResponse(code = 422, message = "Booking was lacking a fillable booking")
    )
    fun createBooking(
        @ApiParam("User id, event id, and a list of tickets")
        @RequestBody
        dto: BookingDto
    ) : ResponseEntity<WrappedResponse<Unit>> {
        if (dto.tickets.isEmpty() ||
            dto.event == null ||
            dto.user == null ) {
            return ResponseEntity.status(422).build()
        }

        val checkTicketsIncludeValidFields: (TicketDto) -> Boolean = {
            it.seat != null || it.price != null
        }

        val tickets = dto.tickets
                .asSequence()
                .filter(checkTicketsIncludeValidFields)
                .toSet()

        if (tickets.isEmpty()) {
            return ResponseEntity.status(422).build()
        }

        val bookingId : Long = bookingService.createBooking(dto)

        return ResponseEntity.created(URI.create("$BASE_PATH/$bookingId")).body(
            WrappedResponse<Unit>(code = 201, message = "Booking was created").validated()
        )
    }

    @ApiOperation("Update a booking with a given booking id")
    @PutMapping(path = ["/{id}"], consumes = [BASE_JSON])
    @ApiResponses(
        ApiResponse(code = 200, message = "Booking ticket was updated"),
        ApiResponse(code = 400, message = "Ticket id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found"),
        ApiResponse(code = 409, message = "Conflict"),
        ApiResponse(code = 500, message = "Server is experiencing issues")
    )
    fun updateFullBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String,

        @ApiParam("Booking information including ticket, event, user and id")
        @RequestBody
        dto: BookingDto
    ) : ResponseEntity<WrappedResponse<Unit>> {
        val id: Long

        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        if (dto.id == null) {
            return ResponseEntity.status(400).build()
        }

        if (dto.id != id) {
            return ResponseEntity.status(409).build()
        }

        val bookingDto = bookingService.findById(id = id, withTickets = true)

        if (bookingDto.id == null) {
            return ResponseEntity.status(404).build()
        }

        bookingDto.user = dto.user!!
        bookingDto.event = dto.event!!
        bookingDto.tickets = dto.tickets

        val isUpdated = bookingService.updateBooking(bookingDto)

        return if (isUpdated) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(500).build()
        }
    }

    @ApiOperation("Update a booking with the given id")
    @PatchMapping(path = ["/{id}"], consumes = [MERGE_PATCH])
    @ApiResponses(
        ApiResponse(code = 204, message = "Booking was updated"),
        ApiResponse(code = 400, message = "Booking patch was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found"),
        ApiResponse(code = 409, message = "Conflict"),
        ApiResponse(code = 500, message = "Server is experiencing issues")
    )
    fun mergePatchBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String,

        @ApiParam("The partial patch")
        @RequestBody
        jsonPatch: String
    ) : ResponseEntity<String> {
        val id: Long

        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        val dto = bookingService.findById(id = id, withTickets = true)

        if (dto.id == null) {
            return ResponseEntity.status(404).build()
        }

        val jackson = ObjectMapper()
        val jsonNode : JsonNode

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (jsonNode.has("id")) {
            return ResponseEntity.status(409).build()
        }

        var newUser = dto.user
        var newEvent = dto.event

        if (jsonNode.has("user")) {
            val node = jsonNode.get("user")

            newUser = when {
                node.isNull -> null
                node.isLong -> node.asLong()
                else -> return ResponseEntity.status(400).build()
            }
        }

        if (jsonNode.has("event")) {
            val node = jsonNode.get("event")

            newEvent = when {
                node.isNull -> null
                node.isLong -> node.asLong()
                else -> return ResponseEntity.status(400).build()
            }
        }

        dto.user = newUser
        dto.event = newEvent

        val isUpdated = bookingService.updateBooking(dto)

        return if (isUpdated) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(500).build()
        }
    }

    @ApiOperation("Update a booking with the given id")
    @PatchMapping(path = ["/{bookingId}/tickets/{ticketId}"], consumes = [BASE_JSON])
    @ApiResponses(
        ApiResponse(code = 204, message = "Ticket was updated"),
        ApiResponse(code = 400, message = "Ticket patch was not processable"),
        ApiResponse(code = 404, message = "Items with specified ids were not found")
    )
    fun mergePatchTicket(
        @ApiParam(ID_PARAM)
        @PathVariable("bookingId")
        pathBookingId: String,

        @ApiParam("The numeric id of the ticket")
        @PathVariable("ticketId")
        pathTicketId: String,

        @ApiParam("The partial patch")
        @RequestBody
        jsonPatch: String
    ) : ResponseEntity<String> {
        val bookingId : Long
        val ticketId : Long

        try {
            bookingId = pathBookingId.toLong()
            ticketId = pathTicketId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        val bookingDto = bookingService.findById(id = bookingId, withTickets = true)

        if (bookingDto.id == null) {
            return ResponseEntity.status(404).build()
        }

        val isTicketPresent = bookingDto.tickets.any { it.id == ticketId }

        if (!isTicketPresent) {
            return ResponseEntity.status(404).build()
        }

        val ticketDto = ticketService.findById(ticketId)

        if (ticketDto.id == null) {
            return ResponseEntity.status(404).build()
        }

        val jackson = ObjectMapper()
        val jsonNode : JsonNode

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (jsonNode.has("id")) {
            return ResponseEntity.status(409).build()
        }

        var newPrice = ticketDto.price
        var newSeat = ticketDto.seat

        if (jsonNode.has("price")) {
            val node = jsonNode.get("price")

            newPrice = when {
                node.isNull -> null
                node.isDouble -> node.asDouble()
                else -> return ResponseEntity.status(400).build()
            }
        }

        if (jsonNode.has("seat")) {
            val node = jsonNode.get("seat")

            newSeat = when {
                node.isNull -> null
                node.isTextual -> node.asText()
                else -> return ResponseEntity.status(400).build()
            }
        }

        ticketDto.price = newPrice
        ticketDto.seat = newSeat

        val isUpdated = ticketService.updateTicket(ticketDto)

        return if (isUpdated) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(500).build()
        }
    }

    @ApiOperation("Delete a booking with the given id")
    @DeleteMapping(path = ["/{id}"])
    @ApiResponses(
        ApiResponse(code = 204, message = "Booking was deleted, and is not returning any content"),
        ApiResponse(code = 400, message = "Booking id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found")
    )
    fun deleteBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String
    ) : ResponseEntity<WrappedResponse<Unit>> {
        val id: Long

        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).build()
        }

        val bookingWasDeleted = bookingService.deleteBookingById(id)

        if (!bookingWasDeleted) {
            return ResponseEntity.status(404).build()
        }

        return ResponseEntity.status(204).body(
            WrappedResponse<Unit>(code = 204, message = "Booking with id $id was deleted").validated()
        )
    }
}