package org.bjh.movies

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.dto.MovieDto
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.Month
import kotlin.collections.ArrayList

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(MoviesApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class TestBase {

    @LocalServerPort
    protected var port = 0


    @Autowired
    protected lateinit var repository: MoviesRepository

    private val movieEntities = ArrayList<MovieEntity>()

    val defaultReleaseDate = LocalDate.of(1991, Month.JANUARY, 12)!!

    fun emptyListAndFillWithTestMovies() {
        movieEntities.removeAll(movieEntities)
        movieEntities.add(MovieEntity("The Hitchhiker's Guide to the Galaxy", "Link to poster", "123", "123", "123", defaultReleaseDate, setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("A Movie About A Ginger", "Link to poster", "123", "123", "123", defaultReleaseDate, setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("Fast and the Furious", "link to poster", "123", "123", "123", defaultReleaseDate, setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("Moon Moon", "Link to poster", "123", "123", "123", defaultReleaseDate, setOf("123"), 1, 5.0, 3.4, 120.0))
        movieEntities.add(MovieEntity("The Life of Pi", "Link to poster", "123", "123", "123", defaultReleaseDate, setOf("123"), 1, 5.0, 3.4, 120.0))
    }

    fun getAllMovies(): MutableList<MovieDto>? {
        return given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)
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
            movieEntities.forEach {
                save(it)
            }
        }
    }
}
