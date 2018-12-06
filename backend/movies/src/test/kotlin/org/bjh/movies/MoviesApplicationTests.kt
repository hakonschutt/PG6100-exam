package org.bjh.movies

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.Month
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

    @Test
    fun testGetAll() {
        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(5))
    }

    @Test
    fun testGetNonExistingMovie() {
        given().accept(ContentType.JSON)
                .get("-1")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", not(equalTo(null)))
    }

    @Test
    fun testGetMovie() {
        getAllMovies()?.parallelStream()?.forEach { it ->
            assertThat(given().accept(ContentType.JSON)
                    .get(it.id)
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("data.list", MovieDto::class.java)[0], equalTo(it))
        }
    }

    @Test
    fun testCreateMovie() {
        val testTitle = "TestMovie"
        val allMovies = getAllMovies()

        val location = given().contentType(ContentType.JSON)
                .body(MovieDto(testTitle, "posterURL", "coverArtUrl", "trailerURL", "Test Overview", defaultReleaseDate.toString(), setOf("Drama"), 1, "5.0", "200.0", "120.0"))
                .post()
                .then()
                .statusCode(201)
                .extract()
                .header("location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.list[0].title", equalTo(testTitle))

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(allMovies!!.size + 1))

    }

    @Test
    fun testUpdateExistingMovieWithPut()  {
        val allMovies = getAllMovies()
        val movie = allMovies!![0]
        val modifiedTitle = "NEW TITLE"
        val alteredMovieToPut = movie.copy(title=modifiedTitle)

        given().contentType(ContentType.JSON)
                .body(alteredMovieToPut)
                .put(movie.id)
                .then()
                .statusCode(204)

        given().accept(ContentType.JSON)
                .get(movie.id)
                .then()
                .statusCode(200)
                .body("data.list[0].title", not(equalTo(modifiedTitle)))
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
                .body("data.list.size()", equalTo(allMovies.size - 1))
    }

    @Test
    fun testDeleteAllMovies() {
        val allMovies = getAllMovies()

        allMovies!!
                .parallelStream()
                .forEach {
                    given().accept(ContentType.JSON)
                            .delete("${it.id}")
                            .then()
                            .statusCode(204)
                }

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(0))
    }

    @Test
    fun testUpdateSpecificMovieWithPatch() {
        val oldTitle = "OldMovieTitle"
        val newTitle = "NewMovieTitle"
        val posterUrl = "posterURL"
        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val movieDto = MovieDto(
                oldTitle,
                posterUrl,
                "coverArtUrl",
                "trailerURL",
                "Test Overview",
                defaultReleaseDate.toString(),
                setOf("Drama"),
                1,
                "5.0",
                "200.0",
                "120.0")


        given().contentType(ContentType.JSON)
                .body(movieDto)
                .post()

        val movieList = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)


        val id = movieList[5].id

        val originalMovieDto = given().accept(ContentType.JSON)
                .get("$id")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0]

        val patchBody = "{\"title\":\"$newTitle\", \"poster\":null}"

        repeat(1000) {
            given().contentType("application/merge-patch+json")
                    .body(patchBody)
                    .patch("$id")
                    .then()
                    .statusCode(204)

            val patchedMovieDto = given().accept(ContentType.JSON)
                    .get("$id")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("data.list", MovieDto::class.java)[0]

            val comparableMovieDto = patchedMovieDto.copy(title = oldTitle, poster = posterUrl)

            assertThat(patchedMovieDto, not(equalTo(originalMovieDto)))
            assertThat(patchedMovieDto.title, not(equalTo(oldTitle)))
            assertThat(patchedMovieDto.title, equalTo(newTitle))
            assertThat(originalMovieDto.poster, not(nullValue()))
            assertThat(patchedMovieDto.poster, nullValue())
            assertThat(comparableMovieDto, equalTo(originalMovieDto))
        }
    }
}

