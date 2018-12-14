package org.bjh.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.*
import org.bjh.dto.BookingDto
import org.bjh.dto.TicketDto
import org.bjh.pagination.PageDto
import org.bjh.service.BookingService
import org.bjh.service.TicketService
import org.bjh.wrappers.WrappedResponse
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

const val ID_PARAM = "The numeric id of the booking"
const val BASE_JSON = "application/json;charset=UTF-8"
const val MERGE_PATCH = "application/merge-patch+json"

/**
 * @author hakonschutt
 */
@RestController
@RequestMapping(
        path = ["/bookings"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@Api(value = "/bookings", description = "REST endpoints for bookings")
class BookingApi {

    @Autowired
    private lateinit var bookingService : BookingService

    @Autowired
    private lateinit var ticketService: TicketService

    private val BASE_PATH = "/bookings"

//    /**
//     * @author arcuri82
//     */
//    @RabbitListener(queues = ["#{queue.name}"])
//    fun receiveFromAMQP(message: String) {
//        print(message)
//    }

    private fun doWork(input: String) {
        //TODO: Implement stuff that it does.
        input.toCharArray()
                .filter { it == '.' }
                .forEach { Thread.sleep(1000) }
    }

    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    @ApiOperation("Get all the bookings")
    @ApiResponses(
            ApiResponse(code = 200, message = "Returns a list of all bookings"),
            ApiResponse(code = 422, message = "Query parameters are incorrect")
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
    ): ResponseEntity<WrappedResponse<PageDto<BookingDto>>> {
        var userId : Long = -1L
        var eventId : Long = -1L

        println("##########################")
        println("LOOK FOR ME")
        println("##########################")

        try {
            if (!pathEventId.isNullOrBlank()) { eventId = pathEventId!!.toLong() }
            if (!pathUserId.isNullOrBlank()) { userId = pathUserId!!.toLong() }
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(422).body(
                    WrappedResponse<PageDto<BookingDto>>(code = 422, message = "Unprocessable ids").validated()
            )
        }

        val result = if (eventId == -1L && userId == -1L) {
            bookingService.findAll(withTickets = withTickets, offset =  offset, limit = limit)
        } else if (eventId != -1L && userId != -1L) {
            bookingService.findAllByEventIdAndUserId(withTickets = withTickets, eventId = eventId, userId = userId, offset =  offset, limit = limit)
        } else if (eventId != -1L) {
            bookingService.findAllByEventId(withTickets = withTickets, eventId = eventId, offset =  offset, limit = limit)
        } else {
            bookingService.findAllByUserId(withTickets = withTickets, userId = userId, offset =  offset, limit = limit)
        }

        val wrappedResponse = WrappedResponse(code = 200, data = result, message = "list of bookings").validated()

        return ResponseEntity.status(200).body(wrappedResponse)
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
            return ResponseEntity.status(422).body(
                    WrappedResponse<BookingDto>(code = 422, message = "Booking id is of invalid type").validated()
            )
        }

        val booking = bookingService.findById(id, withTickets)

        result = if (booking.id != null) {
            ResponseEntity.status(200).body(
                    WrappedResponse(code = 200, data = booking, message = "booking with id $pathId").validated()
            )
        } else {
            ResponseEntity.status(404).body(
                    WrappedResponse<BookingDto>(code = 404, message = "Booking was not found").validated()
            )
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
    ) : ResponseEntity<WrappedResponse<Long>> {
        if (dto.tickets.isEmpty() ||
                dto.event == null ||
                dto.user == null ) {
            return ResponseEntity.status(422).body(
                    WrappedResponse<Long>(code = 422, message = "Unprocessable booking values").validated()
            )
        }

        val checkTicketsIncludeValidFields: (TicketDto) -> Boolean = {
            it.seat != null || it.price != null
        }

        val tickets = dto.tickets
                .asSequence()
                .filter(checkTicketsIncludeValidFields)
                .toSet()

        if (tickets.isEmpty()) {
            return ResponseEntity.status(422).body(
                    WrappedResponse<Long>(code = 422, message = "Unprocessable tickets").validated()
            )
        }

        val bookingId : Long = bookingService.createBooking(dto)

        return ResponseEntity.status(201).body(
                WrappedResponse(code = 201, data = bookingId, message = "Booking was created").validated()
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
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to process id type").validated()
            )
        }

        if (dto.id == null) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to read booking id").validated()
            )
        }

        if (dto.id != id) {
            return ResponseEntity.status(409).body(
                    WrappedResponse<Unit>(code = 409, message = "Can not override original id value").validated()
            )
        }

        val bookingDto = bookingService.findById(id = id, withTickets = true)

        if (bookingDto.id == null) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Unable to located original value").validated()
            )
        }

        bookingDto.user = dto.user!!
        bookingDto.event = dto.event!!
        bookingDto.tickets = dto.tickets

        val isUpdated = bookingService.updateBooking(bookingDto)

        return if (isUpdated) {
            ResponseEntity.status(204).body(
                    WrappedResponse<Unit>(code = 204, message = "Booking was fully updated").validated()
            )
        } else {
            ResponseEntity.status(500).body(
                    WrappedResponse<Unit>(code = 500, message = "Internal error").validated()
            )
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
    ) : ResponseEntity<WrappedResponse<Unit>> {
        val id: Long

        try {
            id = pathId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Booking ids were of incorrect type").validated()
            )
        }

        val dto = bookingService.findById(id = id, withTickets = true)

        if (dto.id == null) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Unable to find booking with id $id").validated()
            )
        }

        val jackson = ObjectMapper()
        val jsonNode : JsonNode

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to read patch values").validated()
            )
        }

        if (jsonNode.has("id")) {
            return ResponseEntity.status(409).body(
                    WrappedResponse<Unit>(code = 409, message = "Unable to overwrite body id").validated()
            )
        }

        var newUser = dto.user
        var newEvent = dto.event

        if (jsonNode.has("user")) {
            val node = jsonNode.get("user")

            try {
                newUser = node.toString().toLong()
            } catch (e: NumberFormatException) {
                return ResponseEntity.status(400).body(
                        WrappedResponse<Unit>(code = 400, message = "User value was of incorrect type").validated()
                )
            }
        }

        if (jsonNode.has("event")) {
            val node = jsonNode.get("event")

            try {
                newEvent = node.toString().toLong()
            } catch (e: NumberFormatException) {
                return ResponseEntity.status(400).body(
                        WrappedResponse<Unit>(code = 400, message = "Event value was of incorrect type").validated()
                )
            }
        }

        dto.user = newUser
        dto.event = newEvent

        val isUpdated = bookingService.updateBooking(dto)

        return if (isUpdated) {
            ResponseEntity.status(204).body(
                    WrappedResponse<Unit>(code = 204, message = "Booking was updated").validated()
            )
        } else {
            ResponseEntity.status(500).body(
                    WrappedResponse<Unit>(code = 500, message = "Internal error").validated()
            )
        }
    }

    @ApiOperation("Update a booking with the given id")
    @PatchMapping(path = ["/{bookingId}/tickets/{ticketId}"], consumes = [MERGE_PATCH])
    @ApiResponses(
            ApiResponse(code = 204, message = "Ticket was updated"),
            ApiResponse(code = 400, message = "Ticket patch was not processable"),
            ApiResponse(code = 404, message = "Items with specified ids were not found"),
            ApiResponse(code = 409, message = "Conflict")
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
    ) : ResponseEntity<WrappedResponse<Unit>> {
        val bookingId : Long
        val ticketId : Long

        try {
            bookingId = pathBookingId.toLong()
            ticketId = pathTicketId.toLong()
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Booking ids were of incorrect type").validated()
            )
        }

        val bookingDto = bookingService.findById(id = bookingId, withTickets = true)

        if (bookingDto.id == null) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Unable to find booking instance").validated()
            )
        }

        val isTicketPresent = bookingDto.tickets.any { it.id == ticketId }

        if (!isTicketPresent) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Ticket was not present on booking").validated()
            )
        }

        val ticketDto = ticketService.findById(ticketId)

        if (ticketDto.id == null) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Ticket was not located in the database").validated()
            )
        }

        val jackson = ObjectMapper()
        val jsonNode : JsonNode

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to process sent object").validated()
            )
        }

        if (jsonNode.has("id")) {
            return ResponseEntity.status(409).body(
                    WrappedResponse<Unit>(code = 409, message = "Unable to overwrite body id").validated()
            )
        }

        var newPrice = ticketDto.price
        var newSeat = ticketDto.seat

        if (jsonNode.has("price")) {
            val node = jsonNode.get("price")

            newPrice = when {
                node.isNull -> null
                node.isDouble -> node.asDouble()
                else -> return ResponseEntity.status(400).body(
                        WrappedResponse<Unit>(code = 400, message = "Price value was of incorrect type").validated()
                )
            }
        }

        if (jsonNode.has("seat")) {
            val node = jsonNode.get("seat")

            newSeat = when {
                node.isNull -> null
                node.isTextual -> node.asText()
                else -> return ResponseEntity.status(400).body(
                        WrappedResponse<Unit>(code = 400, message = "Seat value was of incorrect type").validated()
                )
            }
        }

        ticketDto.price = newPrice
        ticketDto.seat = newSeat

        val isUpdated = ticketService.updateTicket(ticketDto)

        return if (isUpdated) {
            ResponseEntity.status(204).body(
                    WrappedResponse<Unit>(code = 204, message = "Ticket with is $ticketId was updated").validated()
            )
        } else {
            ResponseEntity.status(500).body(
                    WrappedResponse<Unit>(code = 500, message = "Internal error").validated()
            )
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
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Booking id was not of correct type").validated()
            )
        }

        val bookingWasDeleted = bookingService.deleteBookingById(id)

        if (!bookingWasDeleted) {
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(code = 404, message = "Unable to delete booking with id $id").validated()
            )
        }

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(code = 204, message = "Booking with id $id was deleted").validated()
        )
    }
}