package org.bjh

import io.restassured.RestAssured
import org.bjh.repository.BookingRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author hakonschutt
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(BookingApplicationRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class TestBase {

    @Autowired
    protected lateinit var bookingRepository : BookingRepository

    @Autowired
    protected lateinit var cacheManager: CacheManager

    @LocalServerPort
    protected var port = 0

    fun prepTestData() {
        bookingRepository.run {
            deleteAll()
        }
    }

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/bookings"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        prepTestData()

        if (cacheManager.getCache("bookingCache") != null) {
            cacheManager.getCache("bookingCache").clear()
        }
    }
}