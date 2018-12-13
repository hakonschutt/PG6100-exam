package org.bjh.api

import io.restassured.RestAssured.given
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.bjh.TestBase
import org.bjh.dto.BookingDto
import org.bjh.dto.TicketDto
import org.bjh.entity.BookingEntity
import org.bjh.entity.TicketEntity
import org.bjh.pagination.PageDto
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotEquals
import org.junit.Test



class BookingApiTest : TestBase() {

    @Test
    fun testGetAllBookingsByUserIdAndEventIdWithUnprocessableEntities() {
        given()
            .get("?withTickets=false&eventId=fail&userId=fail")
            .then()
            .statusCode(422)
            .body("message", equalTo("Unprocessable ids"))
            .body("code", equalTo(422))
    }

    @Test
    fun testGetAllBookings() {
        val list = testGetAllBookingsRequest("", 4)

        list.stream().forEach{ assertTrue(it is BookingDto) }
    }

    @Test
    fun testGetAllBookingsByEventId() {
        val list = testGetAllBookingsRequest("?withTickets=false&eventId=2", 2)

        list.stream().forEach{ assertEquals(it.event, 2L) }
    }

    @Test
    fun testGetAllBookingsByUserId() {
        val list = testGetAllBookingsRequest("?withTickets=false&userId=2", 3)

        list.stream().forEach{ assertEquals(it.user, 2L) }
    }

    @Test
    fun testGetAllBookingsByUserIdAndEventId() {
        val list = testGetAllBookingsRequest("?withTickets=false&userId=2&eventId=2", 1)

        list.stream().forEach{
            assertEquals(it.event, 2L)
            assertEquals(it.user, 2L)
        }
    }

    @Test
    fun testPagination() {
        generateBooking(1L, 2L)
        generateBooking(2L, 3L)

        val pageDto =  given()
            .get("/?withTickets=false&offset=0&limit=1")
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(1))
            .body("data.totalSize", equalTo(1))
            .extract()
            .body()
            .jsonPath()
            .getObject("data", PageDto::class.java)

        val nextPageDto = given()
            .get(pageDto._links["next"]!!.href.substring(9))
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(1))
            .body("data.totalSize", equalTo(1))
            .extract()
            .body()
            .jsonPath()
            .getObject("data", PageDto::class.java)

        val previousPageDto =  given()
            .get(nextPageDto.previous!!.href.substring(9))
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(1))
            .body("data.totalSize", equalTo(1))
            .extract()
            .body()
            .jsonPath()
            .getObject("data", PageDto::class.java)

        assertNotEquals(pageDto.list[0], nextPageDto.list[0])
        assertEquals(pageDto.list[0], previousPageDto.list[0])
    }

    @Test
    fun testGetBookingByIdWithUnprocessableId() {
        given()
            .get("/fail?withTickets=false")
            .then()
            .statusCode(422)
            .body("message", equalTo("Booking id is of invalid type"))
            .body("code", equalTo(422))
    }

    @Test
    fun testGetBookingById() {
        generateBooking(1L, 2L)

        val data = given()
            .get("?withTickets=true")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .jsonPath()
            .getList("data.list", BookingDto::class.java)[0]

        val booking = given()
            .get("/${data.id}?withTickets=true")
            .then()
            .statusCode(200)
            .body("message", equalTo("booking with id ${data.id}"))
            .extract()
            .body()
            .jsonPath()
            .getObject("data", BookingDto::class.java)

        assertThat(data, equalTo(booking))
    }

    @Test
    fun testGetBookingByIdWithNotFoundId() {
        // NO data is added so will return 404 no matter what id
        given()
            .get("/1?withTickets=false")
            .then()
            .statusCode(404)
            .body("message", equalTo("Booking was not found"))
            .body("code", equalTo(404))
    }

    @Test
    fun testCreateBookingWithFaultValues() {
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf())

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .post()
            .then()
            .statusCode(422)
            .body("message", equalTo("Unprocessable booking values"))
            .body("code", equalTo(422))
    }

    @Test
    fun testCreateBookingWithFaultTicketValues() {
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(TicketDto(id = null, seat = null, price =  null)))

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .post()
            .then()
            .statusCode(422)
            .body("message", equalTo("Unprocessable tickets"))
            .body("code", equalTo(422))
    }

    @Test
    fun testCreateBooking() {
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(TicketDto(id = null, seat = "A2", price = 20.00 )))

        val id = given()
            .contentType(BASE_JSON)
            .body(booking)
            .post()
            .then()
            .statusCode(201)
            .body("message", equalTo("Booking was created"))
            .extract()
            .body()
            .jsonPath()
            .getLong("data")

        val data = given()
            .get("/$id?withTickets=true")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .jsonPath()
            .getObject("data", BookingDto::class.java)

        assertThat(data.event, equalTo(booking.event))
        assertThat(data.user, equalTo(booking.user))
    }

    @Test
    fun testPutBookingWithFaultyId() {
        val ticket = TicketDto(id = null, seat = "A2", price = 20.00 )
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(ticket))

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .put("/fail")
            .then()
            .statusCode(400)
            .body("message", equalTo("Unable to process id type"))
            .body("code", equalTo(400))
    }

    @Test
    fun testPutBookingWithoutId() {
        val ticket = TicketDto(id = null, seat = "A2", price = 20.00 )
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(ticket))

        val id = given()
            .contentType(BASE_JSON)
            .body(booking)
            .post()
            .then()
            .statusCode(201)
            .body("message", equalTo("Booking was created"))
            .extract()
            .body()
            .jsonPath()
            .getLong("data")

        booking.event = 2L
        booking.user = 1L
        booking.id = null

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .put("/$id")
            .then()
            .statusCode(400)
            .body("message", equalTo("Unable to read booking id"))
            .body("code", equalTo(400))
    }

    @Test
    fun testPutBookingWithDifferentIds() {
        val ticket = TicketDto(id = null, seat = "A2", price = 20.00 )
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(ticket))

        val id = given()
                .contentType(BASE_JSON)
                .body(booking)
                .post()
                .then()
                .statusCode(201)
                .body("message", equalTo("Booking was created"))
                .extract()
                .body()
                .jsonPath()
                .getLong("data")

        booking.event = 2L
        booking.user = 1L
        booking.id = id * 2

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .put("/$id")
            .then()
            .statusCode(409)
            .body("message", equalTo("Can not override original id value"))
            .body("code", equalTo(409))
    }

    @Test
    fun testPutBooking() {
        val ticket = TicketDto(id = null, seat = "A2", price = 20.00 )
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(ticket))

        val id = given()
                .contentType(BASE_JSON)
                .body(booking)
                .post()
                .then()
                .statusCode(201)
                .body("message", equalTo("Booking was created"))
                .extract()
                .body()
                .jsonPath()
                .getLong("data")

        booking.event = 2L
        booking.user = 1L
        booking.id = id

        given()
            .contentType(BASE_JSON)
            .body(booking)
            .put("/$id")
            .then()
            .statusCode(204)

        val data = given()
            .get("/$id?withTickets=true")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .jsonPath()
            .getObject("data", BookingDto::class.java)

        data.tickets.stream().forEach {
            assertThat(it.seat, equalTo(ticket.seat))
            assertThat(it.price, equalTo(ticket.price))
        }

        assertThat(data.event, equalTo(booking.event))
        assertThat(data.user, equalTo(booking.user))
    }

    @Test
    fun testMergePatchBooking() {
        val originalEvent = 2L
        val originalUser = 2L
        val ticket = TicketDto(id = null, seat = "2A", price = 20.00)
        val booking = BookingDto(id = null, event = originalEvent, user = originalUser, tickets = setOf(ticket))

        val id = given()
                .contentType(BASE_JSON)
                .body(booking)
                .post()
                .then()
                .statusCode(201)
                .body("message", equalTo("Booking was created"))
                .extract()
                .body()
                .jsonPath()
                .getLong("data")

        val newEvent = 10L
        val jsonBody = "{\"event\": $newEvent}"

        given()
            .contentType("application/merge-patch+json")
            .body(jsonBody)
            .patch("/$id")
            .then()
            .statusCode(204)

        val newBooking = given()
                .get("/$id")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", BookingDto::class.java)

        assertThat(newBooking.event, equalTo(newEvent))
        assertThat(newBooking.user, equalTo(originalUser))
    }

    @Test
    fun testMergePatchBookingTicket() {
        val originalPrice = 20.00
        val originalSeat = "2A"
        val ticket = TicketDto(id = null, seat = originalSeat, price = originalPrice)
        val booking = BookingDto(id = null, event = 1L, user = 2L, tickets = setOf(ticket))

        val id = given()
                .contentType(BASE_JSON)
                .body(booking)
                .post()
                .then()
                .statusCode(201)
                .body("message", equalTo("Booking was created"))
                .extract()
                .body()
                .jsonPath()
                .getLong("data")

        val ticketDto = given()
                .get("/$id?withTickets=true")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("data.tickets", TicketDto::class.java)[0]

        val newSeat = "4C"
        val newPrice = 45.50

        val jsonBody = "{\"seat\": \"$newSeat\", \"price\": $newPrice}"

        given()
            .contentType("application/merge-patch+json")
            .body(jsonBody)
            .patch("/$id/tickets/${ticketDto.id}")
            .then()
            .statusCode(204)

        val newTicketDto = given()
                .get("/$id?withTickets=true")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("data.tickets", TicketDto::class.java)[0]
        println(ticketDto)
        println(newTicketDto)
        assertThat(newTicketDto.seat, equalTo("4C"))
        assertThat(newTicketDto.price, equalTo(45.50))
    }

    @Test
    fun testDeleteBookingWithFauiltyId() {
        given()
            .delete("/fail")
            .then()
            .statusCode(400)
            .body("message", equalTo("Booking id was not of correct type"))
            .body("code", equalTo(400))
    }

    @Test
    fun testDeleteBookingWithEmptyDataSet() {
        val id = 1L

        given()
            .delete("/$id")
            .then()
            .statusCode(404)
            .body("message", equalTo("Unable to delete booking with id $id"))
            .body("code", equalTo(404))
    }

    @Test
    fun testDeleteBooking() {
        generateBooking(1L, 2L)

        val data = given()
            .get()
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(1))
            .extract()
            .body()
            .jsonPath()
            .getList("data.list", BookingDto::class.java)[0]

        given()
            .delete("/${data.id}")
            .then()
            .statusCode(204)

        given()
            .get("/${data.id}")
            .then()
            .statusCode(404)
    }

    fun generateBooking(user: Long, event: Long) {
        val booking = BookingEntity(
                id = null,
                user = user,
                event = event,
                tickets = setOf(
                        TicketEntity(
                                id = null,
                                seat = "seat-0",
                                price = 10.00
                        )
                )
        )

        bookingRepository.save(booking)
    }

    fun testGetAllBookingsRequest(route : String, size : Int) : List<BookingDto> {
        generateBooking(1L, 2L)
        generateBooking(2L, 3L)
        generateBooking(2L, 2L)
        generateBooking(2L, 3L)

        val data = given()
                .get(route)
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(size))
                .body("data.totalSize", equalTo(size))
                .extract()
                .body()
                .jsonPath()
                .getList("data.list", BookingDto::class.java)

        return data as List<BookingDto>
    }
}