package org.bjh

import io.restassured.RestAssured
import org.bjh.repository.BookingRepository
import org.bjh.repository.TicketRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author hakonschutt
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(BookingApplicationRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationRunner {

    @Autowired
    protected lateinit var bookingRepository : BookingRepository

    @Autowired
    protected lateinit var ticketRepository : TicketRepository

    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/bookings"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }
}