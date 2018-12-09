package org.bjh.movies.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.bjh.movies.LocalApplicationRunner
import org.bjh.dto.MovieDto
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class MoviesApiTest : LocalApplicationRunner() {

    @Test
    fun testGetAll() {
        RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(5))
    }

    @Test
    fun testGetNonExistingMovie() {
        RestAssured.given().accept(ContentType.JSON)
                .get("-1")
                .then()
                .statusCode(404)
                .body("code", CoreMatchers.equalTo(404))
                .body("message", CoreMatchers.not(CoreMatchers.equalTo(null)))
    }

    @Test
    fun testGetMovie() {
        getAllMovies()?.parallelStream()?.forEach { it ->
            MatcherAssert.assertThat(RestAssured.given().accept(ContentType.JSON)
                    .get(it.id)
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("data.list", MovieDto::class.java)[0], CoreMatchers.equalTo(it))
        }
    }

    @Test
    fun testCreateMovie() {
        val testTitle = "TestMovie"
        val allMovies = getAllMovies()

        val location = RestAssured.given().contentType(ContentType.JSON)
                .body(MovieDto(testTitle, "posterURL", "coverArtUrl", "trailerURL", "Test Overview", defaultReleaseDate.toString(), setOf("Drama"), 1, "5.0", "200.0", "120.0"))
                .post()
                .then()
                .statusCode(201)
                .extract()
                .header("location")

        RestAssured.given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.list[0].title", CoreMatchers.equalTo(testTitle))

        RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(allMovies!!.size + 1))

    }

    @Test
    fun testUpdateExistingMovieWithPut()  {
        val allMovies = getAllMovies()
        val movie = allMovies!![0]
        val modifiedTitle = "NEW TITLE"
        val alteredMovieToPut = movie.copy(title=modifiedTitle)

        RestAssured.given().contentType(ContentType.JSON)
                .body(alteredMovieToPut)
                .put(movie.id)
                .then()
                .statusCode(204)

        RestAssured.given().accept(ContentType.JSON)
                .get(movie.id)
                .then()
                .statusCode(200)
                .body("data.list[0].title", CoreMatchers.not(CoreMatchers.equalTo(modifiedTitle)))
    }

    @Test
    fun testDeleteOneMovie() {
        val allMovies = getAllMovies()

        RestAssured.given().accept(ContentType.JSON)
                .delete("/${allMovies!![0].id}")
                .then()
                .statusCode(204)

        RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(allMovies.size - 1))
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

        RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(0))
    }

    @Test
    fun testUpdateSpecificMovieWithPatch() {
        val oldTitle = "OldMovieTitle"
        val newTitle = "NewMovieTitle"
        val posterUrl = "posterURL"
        RestAssured.given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", CoreMatchers.equalTo(5))

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


        RestAssured.given().contentType(ContentType.JSON)
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
            RestAssured.given().contentType("application/merge-patch+json")
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

            MatcherAssert.assertThat(patchedMovieDto, CoreMatchers.not(CoreMatchers.equalTo(originalMovieDto)))
            MatcherAssert.assertThat(patchedMovieDto.title, CoreMatchers.not(CoreMatchers.equalTo(oldTitle)))
            MatcherAssert.assertThat(patchedMovieDto.title, CoreMatchers.equalTo(newTitle))
            MatcherAssert.assertThat(originalMovieDto.poster, CoreMatchers.not(CoreMatchers.nullValue()))
            MatcherAssert.assertThat(patchedMovieDto.poster, CoreMatchers.nullValue())
            MatcherAssert.assertThat(comparableMovieDto, CoreMatchers.equalTo(originalMovieDto))
        }
    }
}