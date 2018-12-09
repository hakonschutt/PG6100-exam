package org.bjh.movies.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.movies.LocalApplicationRunner
import org.bjh.dto.MovieDto
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.equalTo
import org.hamcrest.not
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MoviesApiTest : LocalApplicationRunner() {

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
            assertThat(RestAssured.given().accept(ContentType.JSON)
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
                    RestAssured.given().accept(ContentType.JSON)
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
    fun testUpdateSpecificMovieWithNewTitle() {
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

        val movieList = RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)


        val id = movieList[5].id

        val originalMovieDto = RestAssured.given().accept(ContentType.JSON)
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

            val patchedMovieDto = RestAssured.given().accept(ContentType.JSON)
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