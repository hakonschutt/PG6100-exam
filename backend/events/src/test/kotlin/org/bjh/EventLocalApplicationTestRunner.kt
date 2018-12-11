package org.bjh

import io.restassured.RestAssured
import org.bjh.repository.EventRepository
import org.bjh.service.EventService
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

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
//        @LocalServerPort
//        protected var port = 0

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

    private fun stubMovie() = """
            |{
            |"code":200,
            |"data":[
            |   {
            |       "title":"testTitle",
            |       "poster":"p",
            |       "coverArt":"cA",
            |       "trailer":"t",
            |       "overview":"ov",
            |       "releaseDate":"date",
            |       "genre":["drama"],
            |       "voteCount":1,
            |       "voteAverage":"2.2",
            |       "popularity":"pop",
            |       "price":"2.20",
            |       "id": "1"
            |   }
            |],
            |"status":"ok",
            |"message":"Worked"
            |}
            |""".trimMargin()

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


    private fun stubRooms() =
            """
                |{
            |"code":200,
            |"data":[
            |
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
            |],
            |"status":"ok",
            |"message":"Worked"
            |}
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
        val eventJson = """{date: \"Wed Oct 24 2018 18:23:19 GMT+0200 (Central European Summer Time)\",movieId: \"123\",venueId: \"123\",roomId: \"123\",rows: 12,columns: 12}""".trimIndent()
        val id = given().accept(ContentType.JSON)
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
                .body("data.create", equalTo("1"))
                .extract()
                .body()
                .jsonPath()
                .getString("data.create")
        assertEquals("1", id)
    }

    @Test
    fun testGetAllEvents() {

    }

    @Test
    fun testGetEventById() {

    }

    @Test
    fun testGetAllByVenueId() {

    }

    @Test
    fun testGetAllByMovieId() {
    }
}