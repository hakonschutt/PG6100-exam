package org.bjh

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.repository.EventRepository
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/** @author  Kleppa && håkonschutt */
@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EventLocalApplicationTestRunner {

    @Autowired
    protected lateinit var eventRepository: EventRepository

    companion object {

        @Autowired
        private lateinit var wiremockServer: WireMockServer

        @BeforeClass
        @JvmStatic
        fun initClass() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 8084
            RestAssured.basePath = "/graphql"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            wiremockServer = WireMockServer(wireMockConfig().port(8099).notifier(ConsoleNotifier(true)))
            wiremockServer.start()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }
    }

    @Before
    @After
    fun clean() {
        eventRepository.run {
            deleteAll()
        }
    }

    private fun stubMovie() = """
            { code: 200, data: [{ title: "testTitle", poster: "p", coverArt: "cA", trailer: "t", overview:"ov", releaseDate: "date", genre: ["drama"], voteCount: 1, voteAverage: "2.2", popularity: "pop", price: "2.20", id: "1" }], status: "ok", message: "Worked"}
    """.trimIndent()

    private fun stubVenue() = """
            |{
            |"code":200,
            |"data":[
            |   {
            |       "id":"1",
            |       "rooms":[
            |       {
            |           "id":"1",
            |           "name":"sal-1",
            |           "row":10,
            |           "columns":10
            |       }  ,
            |        {
            |           "id":"2",
            |           "name":"sal-2",
            |           "row":10,
            |           "columns":10
            |       }
            |       ],
            |       "geoLocation":"Oslo",
            |       "address":"Nydalen",
            |       "name":"venue-name"
            |   }
            |],
            |"status":"ok",
            |"message":"Worked"
            |}
            |""".trimMargin()


    private fun stubRooms() = """{ code: 200, data:[{ id:"1", name: "sal-1", row: 10,
                       "columns:10
                   }  ,
                    {
                       "id":"2",
                       "name":"sal-2",
                       "row":10,
                       "columns":10
                   }
            ],
            "status":"ok",
            "message":"Worked"
            }
        """.trimIndent()


    private fun stubReq(url: String, json: String) {
        wiremockServer.stubFor(
                WireMock.get(
                        urlMatching(url))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.toByteArray(charset("utf-8")).size)
                                .withBody(json)))
    }

    private fun createEvent(movieId: String, venueId: String, roomId: String) : String {
        val eventJson = """{date: \"Wed Oct 24 2018 18:23:19 GMT+0200 (Central European Summer Time)\",movieId: \"$movieId\",venueId: \"$venueId\",roomId: \"$roomId\",rows: 12,columns: 12}""".trimIndent()

        return given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(
                        """
                    { "query" : "mutation{create(event:$eventJson)}"}
                """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .extract()
                .body()
                .jsonPath()
                .getString("data.create")
    }

    @Test
    fun testGetAllWithEmptySet() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(0))

    }

    @Test
    fun testCreateEvent() {
        val size = given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(0))
                .extract()
                .body()
                .jsonPath()
                .getString("data.allEvents.size()")

        val id = createEvent(movieId = "123", venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(size.toInt() + 1))

        given().accept(ContentType.JSON)
                .queryParam("query",
                """
                    {eventById(eventId:"$id"){id}}"
                """.trimIndent())
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.eventById.id", equalTo(id.toInt()))
    }

    @Test
    fun testGetAllEvents() {
        createEvent(movieId = "123", venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
    }

    @Test
    fun testGetEventById() {
        val id = createEvent(movieId = "123", venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query",
                        """
                    {eventById(eventId:"$id"){id}}"
                """.trimIndent())
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.eventById.id", equalTo(id.toInt()))
    }

    @Test
    fun testGetAllByVenueId() {
        val venueId = "1"
        createEvent(movieId = "123", venueId = venueId, roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents(venue: \"$venueId\"){id, venueId}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].venueId", equalTo(venueId))
    }

    @Test
    fun testGetAllByMovieId() {
        val movieId = "1"
        createEvent(movieId = movieId, venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents(movie: \"$movieId\"){id, movieId}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].movieId", equalTo(movieId))
    }

    @Test
    fun testGetAllWithMovies() {
        val stub = stubMovie()
        stubReq("/api/movies.*", stub)

        val movieId = "1"
        createEvent(movieId = movieId, venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents{id, movieId, movie{ id }}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].movieId", equalTo(movieId))
                .body("data.allEvents[0].movie.id", equalTo(movieId))
    }

    @Test
    fun testGetAllWithVenues() {
        val movieId = "1"
        createEvent(movieId = movieId, venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents(movie: \"$movieId\"){id, movieId}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].movieId", equalTo(movieId))
    }

    @Test
    fun testGetAllWithRoom() {
        val movieId = "1"
        createEvent(movieId = movieId, venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents(movie: \"$movieId\"){id, movieId}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].movieId", equalTo(movieId))
    }

    @Test
    fun testGetAllWithVenueRooms() {
        val movieId = "1"
        createEvent(movieId = movieId, venueId = "123", roomId = "123")

        given().accept(ContentType.JSON)
                .queryParam("query", "{allEvents(movie: \"$movieId\"){id, movieId}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allEvents.size()", equalTo(1))
                .body("data.allEvents[0].movieId", equalTo(movieId))
    }
}