package org.bjh.api

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
        userId: String?,

        @ApiParam("The event id to query by")
        @RequestParam("eventId", required = false)
        eventId: String?,

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
        val result = if (eventId.isNullOrBlank() && userId.isNullOrBlank()) {
            bookingService.findAll(withTickets);
        } else if (!eventId.isNullOrBlank() && !userId.isNullOrBlank()) {
            bookingService.findAllByEventIdAndUserId(withTickets)
        } else if (!eventId.isNullOrBlank()) {
            bookingService.findAllByEventId(withTickets);
        } else {
            bookingService.findAllByUserId(withTickets);
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
            return ResponseEntity.status(422).build();
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

    @ApiOperation("Update a booking ticket with a given booking id")
    @PutMapping(path = ["/{id}/ticket"], consumes = [BASE_JSON])
    @ApiResponses(
        ApiResponse(code = 200, message = "Booking ticket was updated"),
        ApiResponse(code = 400, message = "Booking id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found")
    )
    fun updateBookingTicket(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String?,

        @ApiParam("Ticket information including seat and price")
        @RequestBody
        dto: TicketDto
    ) : ResponseEntity<WrappedResponse<Unit>> {

    }

    @ApiOperation("Update a booking with the given id")
    @PatchMapping(path = ["/{id}"], consumes = [BASE_JSON])
    @ApiResponses(
        ApiResponse(code = 200, message = "Booking was updated"),
        ApiResponse(code = 400, message = "Booking id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found")
    )
    fun updateBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String?
    ) : ResponseEntity<String> {}

    @ApiOperation("Delete a booking with the given id")
    @DeleteMapping(path = ["/{id}"])
    @ApiResponses(
        ApiResponse(code = 200, message = "Booking was deleted"),
        ApiResponse(code = 400, message = "Booking id was not processable"),
        ApiResponse(code = 404, message = "Booking with specified id was not found")
    )
    fun deleteBooking(
        @ApiParam(ID_PARAM)
        @PathVariable("id")
        pathId: String?
    ) : ResponseEntity<Any> {}
}