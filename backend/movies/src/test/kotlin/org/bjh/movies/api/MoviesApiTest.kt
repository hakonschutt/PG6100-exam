package org.bjh.movies.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.movies.TestBase
import org.bjh.dto.MovieDto
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MoviesApiTest : TestBase() {

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
        getAllMovies()?.stream()?.forEach { it ->
           val result = given().accept(ContentType.JSON)
                    .get(it.id)
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("data", MovieDto::class.java)[0]
            assertThat(result, equalTo(it))
        }
    }

    @Test
    fun testGetMovieWithInvalidId() {
        given().accept(ContentType.JSON)
                .get("NOT A VALID ID")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", not(equalTo(null)))
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
                .body("data[0].title", equalTo(testTitle))

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(allMovies!!.size + 1))

    }

    @Test
    fun testUpdateExistingMovieWithPut() {
        val allMovies = getAllMovies()
        val movie = allMovies!![0]
        val modifiedTitle = "NEW TITLE"
        val alteredMovieToPut = movie.copy(title = modifiedTitle)

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
    fun testDeleteMovieWithInvalidId() {
        given().accept(ContentType.JSON)
                .delete("NOT A VALID ID")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", not(equalTo(null)))
    }

    @Test
    fun testDeleteAllMovies() {
        val allMovies = getAllMovies()

        allMovies!!
                .stream()
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

        val movieList = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)


        val id = movieList[5].id

        val originalMovieDto =given().accept(ContentType.JSON)
                .get("$id")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data", MovieDto::class.java)[0]

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
                    .getList("data", MovieDto::class.java)[0]

            val comparableMovieDto = patchedMovieDto.copy(title = oldTitle, poster = posterUrl)

            assertThat(patchedMovieDto, not(equalTo(originalMovieDto)))
            assertThat(patchedMovieDto.title, not(equalTo(oldTitle)))
            assertThat(patchedMovieDto.title, equalTo(newTitle))
            assertThat(originalMovieDto.poster, not(nullValue()))
            assertThat(patchedMovieDto.poster, nullValue())
            assertThat(comparableMovieDto, equalTo(originalMovieDto))
        }
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidId() {
        val invalidId = "NOT A VALID ID"
        val newTitle = "NewMovieTitle"

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"poster\":null}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch(invalidId)
                .then()
                .statusCode(400)
    }


    @Test
    fun testShouldNotUpdateMovieWithIncorrectId() {
        val invalidId = "-1"
        val newTitle = "NewMovieTitle"

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"poster\":null}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch(invalidId)
                .then()
                .statusCode(404)
    }

    @Test
    fun testUpdateSpecificMovieWithMultipleFields() {
        val originalTitle = "OldMovieTitle"
        val originalPosterUrl = "posterURL"
        val newPosterUrl = "NEW posterURL"
        val originalCoverArt = "coverArtUrl"
        val newCoverArt = "NEW coverArtUrl"
        val originalTrailer = "trailerURL"
        val newTrailer = "NEW trailerURL"
        val originalOverview = "Test Overview"
        val newOverview = "NEW Test Overview"
        val originalVoteCount = 1
        val newVoteCount = 2
        val originalPrice = "120.0"
        val newPrice = "150.0"

        val patchBody = """
            {"title":null,
            "poster":"$newPosterUrl",
            "coverArt":"$newCoverArt",
            "trailer":"$newTrailer",
            "overview":"$newOverview",
            "voteCount": $newVoteCount,
            "price": "$newPrice"
            }""".trimIndent()

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val movieDto = MovieDto(
                originalTitle,
                originalPosterUrl,
                originalCoverArt,
                originalTrailer,
                originalOverview,
                defaultReleaseDate.toString(),
                setOf("Drama"),
                originalVoteCount,
                "5.0",
                "200.0",
                originalPrice)


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
                .getList("data", MovieDto::class.java)[0]


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
                .getList("data", MovieDto::class.java)[0]

        val comparableMovieDto = patchedMovieDto.copy(title = originalTitle,
                poster = originalPosterUrl,
                coverArt = originalCoverArt,
                trailer = originalTrailer,
                overview = originalOverview,
                voteCount = originalVoteCount,
                price = originalPrice)

        assertThat(patchedMovieDto, not(equalTo(originalMovieDto)))
        assertThat(patchedMovieDto.title, not(equalTo(originalTitle)))
        assertThat(patchedMovieDto.title, nullValue())
        assertThat(patchedMovieDto.poster, not(equalTo(originalPosterUrl)))
        assertThat(patchedMovieDto.coverArt, not(equalTo(originalCoverArt)))
        assertThat(patchedMovieDto.trailer, not(equalTo(originalTrailer)))
        assertThat(patchedMovieDto.overview, not(equalTo(originalOverview)))
        assertThat(patchedMovieDto.voteCount, not(equalTo(originalVoteCount)))
        assertThat(patchedMovieDto.price, not(equalTo(originalPrice)))
        assertThat(comparableMovieDto, equalTo(originalMovieDto))
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidPoster() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"poster\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidCoverArt() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"coverArt\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidTrailer() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"trailer\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidOverview() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id


        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"overview\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidReleaseDate() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"releaseDate\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidVoteAverage() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"voteAverage\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidPopularity() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"voteAverage\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }

    @Test
    fun testShouldNotUpdateMovieWithInvalidPrice() {
        val newTitle = "NewMovieTitle"

        val id = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", MovieDto::class.java)[0].id

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))

        val patchBody = "{\"title\":\"$newTitle\", \"price\":123}"

        given().contentType("application/merge-patch+json")
                .body(patchBody)
                .patch("$id")
                .then()
                .statusCode(400)
    }
}