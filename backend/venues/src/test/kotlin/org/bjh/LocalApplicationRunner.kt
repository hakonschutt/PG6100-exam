package org.bjh

import io.restassured.RestAssured
import org.bjh.entity.RoomEntity
import org.bjh.entity.VenueEntity
import org.bjh.repository.VenuesRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(VenuesApplicationRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationRunner {

    private val venueEntities = ArrayList<VenueEntity>()
    @Autowired
    protected lateinit var repository: VenuesRepository
    @Autowired
    protected lateinit var cacheManager: CacheManager

    @LocalServerPort
    protected var port = 0

    fun prepTestData() {
        venueEntities.removeAll(venueEntities)

        listOf(1, 2, 3, 4, 5, 6, 7, 8)
                .stream()
                .forEach { data ->
                    venueEntities
                            .add(VenueEntity(
                                    address = "Address -$data",
                                    name = "Venue - $data",
                                    geoLocation = (data * 10).toString(),
                                    rooms = setOf(RoomEntity(id = null,
                                            name = "sal$data",
                                            rows = data,
                                            columns = data * 2)
                                    )
                            ))
                }
    }

    @Before
    @After
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/venues"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        prepTestData()
        cacheManager.getCache("venuesCache").clear()
        repository.run {
            deleteAll()
            venueEntities.forEach {
                save(it)
            }
        }

    }
}
