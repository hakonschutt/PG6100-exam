package org.bjh.movies

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(MoviesApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests {

    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var repository: MoviesRepository

    @Before
    @After
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/movies"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.run {
            deleteAll()
            save(MovieEntity("The Hitchhiker's Guide to the Galaxy", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
            save(MovieEntity("A Movie About A Ginger", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
            save(MovieEntity("Fast and the Furious", "link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
            save(MovieEntity("Moon Moon", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
            save(MovieEntity("The Life of Pi", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
        }
    }


    @Test
    fun testGetAll() {

        RestAssured.given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", CoreMatchers.equalTo(5))
    }


}
