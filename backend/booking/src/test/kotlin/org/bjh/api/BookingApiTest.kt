package org.bjh.api

import io.restassured.RestAssured.given
import org.bjh.LocalApplicationRunner
import org.bjh.dto.BookingDto
import org.bjh.entity.BookingEntity
import org.bjh.entity.TicketEntity
import org.bjh.pagination.PageDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BookingApiTest : LocalApplicationRunner() {

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

    fun testGetAllBookingsRequest(route : String, size : Int) : PageDto<*> {
        generateBooking(1L, 2L)
        generateBooking(2L, 3L)
        generateBooking(2L, 2L)
        generateBooking(2L, 3L)

        return given()
            .get(route)
            .then()
            .statusCode(200)
            .body("data.list.size()", equalTo(size))
            .body("data.totalSize", equalTo(size))
            .extract()
            .body()
            .jsonPath()
            .getObject("data", PageDto::class.java)
    }

    @Test
    fun testGetAllBookings() {
        val data = testGetAllBookingsRequest("", 4)
    }

    @Test
    fun testGetAllBookingsByEventId() {
        val data = testGetAllBookingsRequest("?withTickets=false&eventId=2", 2)
    }

    @Test
    fun testGetAllBookingsByUserId() {
        val data = testGetAllBookingsRequest("?withTickets=false&userId=2", 3)
    }

    @Test
    fun testGetAllBookingsByUserIdAndEventId() {
        val data = testGetAllBookingsRequest("?withTickets=false&userId=2&eventId=2", 1)
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
}