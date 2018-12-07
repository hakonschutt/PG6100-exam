package org.bjh

import io.restassured.RestAssured
import org.bjh.entity.BookingEntity
import org.bjh.entity.TicketEntity
import org.bjh.repository.BookingRepository
import org.bjh.repository.TicketRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.util.Random
import kotlin.collections.ArrayList



/**
 * @author hakonschutt
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(BookingApplicationRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationRunner {

    private val bookingEntities = ArrayList<BookingEntity>()

    @Autowired
    protected lateinit var bookingRepository : BookingRepository

    @Autowired
    protected lateinit var ticketRepository : TicketRepository

    @LocalServerPort
    protected var port = 0

    fun prepTestData() {
        val userList = listOf(1L, 2L, 3L, 4L)
        val eventList = listOf(5L, 6L, 7L, 8L)
        val rand = Random()

        bookingEntities.removeAll(bookingEntities)

        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ,13 ,14 ,15)
            .stream()
            .forEach { data ->
                bookingEntities.add(
                    BookingEntity(
                        id = null,
                        user = userList[rand.nextInt(userList.size)],
                        event = eventList[rand.nextInt(eventList.size)],
                        tickets = setOf(
                            TicketEntity(
                                id = null,
                                seat = "seat-$data",
                                price = 10.00
                            )
                        )
                    )
                )
            }

        bookingRepository.run {
            deleteAll()
            bookingEntities.forEach {
                save(it)
            }
        }
    }

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/bookings"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        prepTestData()
    }
}