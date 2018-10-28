package org.bjh.movies

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.collections.ArrayList

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(MoviesApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests {

    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var repository: MoviesRepository

    private val movieEntities = ArrayList<MovieEntity>()
    
    fun emptyListAndFillWithTestMovies() {
        movieEntities.removeAll(movieEntities)
        movieEntities.add(MovieEntity("The Hitchhiker's Guide to the Galaxy", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("A Movie About A Ginger", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("Fast and the Furious", "link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("Moon Moon", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("The Life of Pi", "Link to poster", "123", "123", "123", Date(), setOf("123"), 1, 5.0, 3.4, 120.0))
    }

    fun getAllMovies(): MutableList<MovieDto>? {
        return given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getList("data", MovieDto::class.java)
    }

    @Before
    @After
    fun clean() {
        emptyListAndFillWithTestMovies()
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/movies"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.run {
            deleteAll()
            movieEntities.forEach{
                save(it)
            }
        }
    }

    @Test
    fun testGetAll() {
        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))
    }

    @Test
    fun testNonExistingMovie() {
        given().accept(ContentType.JSON)
                .get("/-1")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", not(equalTo(null)))
    }

    @Test
    fun testCreateMovie() {
        val testTitle = "TestMovie"
        val allMovies = getAllMovies()

        val location = given().contentType(ContentType.JSON)
                .body(MovieDto(testTitle, "posterURL", "coverArtUrl", "trailerURL", "Test Overview", Date(), setOf("Drama"), 1, 5.0, 200.0, 120.0))
                .post()
                .then()
                .statusCode(201)
                .extract().header("location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.title", equalTo(testTitle))

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(allMovies!!.size + 1))
    }

    @Test
    fun testDeleteOneMovie() {
        val allMovies = getAllMovies()

        given().accept(ContentType.JSON)
                .delete("/${allMovies!![0].id}")
                .then()
                .statusCode(204)

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(allMovies.size - 1))
    }

    @Test
    fun testDeleteAllMovies() {
        val allMovies = getAllMovies()

        allMovies!!
                .parallelStream()
                .forEach {
                    given().accept(ContentType.JSON)
                            .delete("/${it.id}")
                            .then()
                            .statusCode(204)
                }

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(0))
    }
}
