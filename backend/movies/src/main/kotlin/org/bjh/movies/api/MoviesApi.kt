package org.bjh.movies.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.service.MovieService
import org.bjh.pagination.PageDto
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


/**
 * @author bjornosal
 */
@Api(value = "/api/movies", description = "Retrieves movies.")
@RequestMapping(
        path = ["/api/movies"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class MoviesApi {


    @Autowired
    private lateinit var movieService: MovieService

    private val basePath = "/api/movies"

    //TODO: Pagination with infinity-scroll. How to?
    @ApiOperation("Get all the movies")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getAllMovies(@ApiParam("Offset in the list of news")
                     @RequestParam("offset", defaultValue = "0")
                     offset: Int,

                     @ApiParam("Limit of news in a single retrieved page")
                     @RequestParam("limit", defaultValue = "10")
                     limit: Int
    ): ResponseEntity<WrappedResponse<PageDto<MovieDto>>> {

        val list = movieService.getAll(offset, limit)

        return ResponseEntity.ok(
                WrappedResponse(
                        code = 200,
                        data = list)
                        .validated()
        )
    }

    @ApiOperation("Get a specific movie by id")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)],
            path = ["/{id}"])
    fun getMovieById(@ApiParam("Unique ID of a movie")
                     @PathVariable("id")
                     movieId: String): ResponseEntity<WrappedResponse<PageDto<MovieDto>>> {

        val id: Long
        try {
            id = movieId.toLong()
        } catch (ne: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<PageDto<MovieDto>>(
                            code = 400,
                            message = "'$movieId' is not a valid movie id.")
                            .validated())
        }

        val requestResult = movieService.getAllById(id)
        if (requestResult.list.isEmpty() ||
                (requestResult.list.isNotEmpty() && requestResult.list[0].id == null))
            return ResponseEntity.status(404)
                    .body(WrappedResponse<PageDto<MovieDto>>(
                            code = 404,
                            message = "Movie cannot be null")
                            .validated())

        return ResponseEntity.ok(
                WrappedResponse(
                        code = 200,
                        data = requestResult)
                        .validated())

    }

    @ApiOperation("Create a movie")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun createMovie(@ApiParam("Information for new movie")
                    @RequestBody movieDto: MovieDto): ResponseEntity<WrappedResponse<Unit>> {

        val createdId = movieService.createMovie(movieDto)

        if (createdId == -1L)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to create movie.")
                            .validated())


        return ResponseEntity.created(URI.create(basePath + "/" + createdId)).body(
                WrappedResponse<Unit>(code = 201, message = "Movie was created").validated())
    }

    @ApiOperation("Update an existing resource ")
    @PutMapping(path = ["/{id}"])
    fun putMovie(@ApiParam("The id of the movie")
                 @PathVariable("id")
                 movieId: String,

                 @ApiParam("")
                 @RequestBody movieDto: MovieDto
    ): ResponseEntity<WrappedResponse<Unit>> {
        if (movieDto.id != movieId) {
            return ResponseEntity.status(409).build()
        }
        val id: Long
        try {
            id = movieId.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (movieDto.id == null) {
            return ResponseEntity.status(400).build()
        }

        if (movieDto.id != movieId) {
            return ResponseEntity.status(400).build()
        }

        val movieList = movieService.getAllById(id).list

        if (movieList.isEmpty())
            return ResponseEntity.status(400).build()



        movieService.save(movieList[0])
        val stuff = ResponseEntity.status(204).body(
                WrappedResponse<Unit>(
                        code = 204,
                        message = "Updated movie with id: $movieId")
                        .validated())
        print("STUFF "  + stuff)
        return stuff
    }


    //TODO: Check codes being sent here.
    @ApiOperation("Delete a movie by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The id of the movie")
            @PathVariable("id")
            movieId: String
    ): ResponseEntity<WrappedResponse<Unit>> {

        val id: Long

        try {
            id = movieId.toLong()
        } catch (ne: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "'$movieId' is not a valid movie id.")
                            .validated())
        }
        val deleted = movieService.deleteMovieById(id)

        if (!deleted)
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(
                            code = 404,
                            message = "'$movieId' is not an existing movie id.")
                            .validated())

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(
                        code = 204,
                        message = "Deleted movie with id: $movieId")
                        .validated())
    }

    @ApiOperation("Update a specific movie")
    @PatchMapping(path = ["/{id}"],
            consumes = ["application/merge-patch+json"])
    fun updateMovie(
            @ApiParam("The id the of the movie")
            @PathVariable("id")
            movieId: String,
            @ApiParam("Fields that will be updated/added")
            @RequestBody
            fieldsToPatch: String): ResponseEntity<WrappedResponse<Unit>> {
        val id: Long

        try {
            id = movieId.toLong()
        } catch (ne: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "'$movieId' is not a valid movie id.")
                            .validated())
        }


        var movieDto = movieService.getAllById(id).list[0]

        if (movieDto.id == null)
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(
                            code = 404,
                            message = "There was no movie with id $id")
                            .validated())

        val parser = JsonParser()
        val movieObject: JsonObject
        try {
            movieObject = parser.parse(fieldsToPatch).asJsonObject
        } catch (jse: JsonSyntaxException) {
            return ResponseEntity.status(400).build()
        }

        if (movieObject.has("id")) {
            return ResponseEntity.status(409).build()
        }

        val tempDto = movieDto.copy()

        //TODO: Figure out a way to make all this into a repeatable method.
        //TODO: Set a primitive value based on an object based on the name of the field generically,
        if (movieObject.has("title")) {
            val title = movieObject.get("title")

            if (title.isJsonNull)
                tempDto.title = null
            else if (title.isJsonPrimitive && title.asJsonPrimitive.isString)
                tempDto.title = title.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("poster")) {
            val poster = movieObject.get("poster")
            if (poster.isJsonNull)
                tempDto.poster = null
            else if (poster.isJsonPrimitive && poster.asJsonPrimitive.isString)
                tempDto.poster = poster.asString
            else
                return ResponseEntity.status(400).build()
        }


        if (movieObject.has("coverArt")) {
            val coverArt = movieObject.get("coverArt")
            if (coverArt.isJsonNull)
                tempDto.title = null
            else if (coverArt.isJsonPrimitive && coverArt.asJsonPrimitive.isString)
                tempDto.coverArt = coverArt.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("trailer")) {
            val trailer = movieObject.get("trailer")
            if (trailer.isJsonNull)
                tempDto.trailer = null
            else if (trailer.isJsonPrimitive && trailer.asJsonPrimitive.isString)
                tempDto.trailer = trailer.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("overview")) {
            val overview = movieObject.get("overview")
            if (overview.isJsonNull)
                tempDto.overview = null
            else if (overview.isJsonPrimitive && overview.asJsonPrimitive.isString)
                tempDto.overview = overview.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("releaseDate")) {
            val releaseDate = movieObject.get("releaseDate")
            if (releaseDate.isJsonNull)
                tempDto.releaseDate = null
            else if (releaseDate.isJsonPrimitive && releaseDate.asJsonPrimitive.isString)
                tempDto.releaseDate = releaseDate.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("genres")) {
            val genres = movieObject.get("genres")
            when {
                genres.isJsonNull -> tempDto.genres = null
                genres.isJsonArray -> setOf(genres.asJsonArray)
                else -> return ResponseEntity.status(400).build()
            }
        }


        if (movieObject.has("voteCount")) {
            val voteCount = movieObject.get("voteCount")
            if (voteCount.isJsonNull)
                tempDto.voteCount = null
            else if (voteCount.isJsonPrimitive && voteCount.asJsonPrimitive.isNumber)
                tempDto.voteCount = voteCount.asInt
            else
                return ResponseEntity.status(400).build()
        }

        //TODO Is this correct for double?
        if (movieObject.has("voteAverage")) {
            val voteAverage = movieObject.get("voteAverage")
            if (voteAverage.isJsonNull)
                tempDto.voteAverage = null
            else if (voteAverage.isJsonPrimitive && voteAverage.asJsonPrimitive.isString)
                tempDto.voteAverage = voteAverage.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("popularity")) {
            val popularity = movieObject.get("popularity")
            if (popularity.isJsonNull)
                tempDto.popularity = null
            else if (popularity.isJsonPrimitive && popularity.asJsonPrimitive.isString)
                tempDto.popularity = popularity.asString
            else
                return ResponseEntity.status(400).build()
        }

        if (movieObject.has("price")) {
            val price = movieObject.get("price")
            if (price.isJsonNull)
                tempDto.price = null
            else if (price.isJsonPrimitive && price.asJsonPrimitive.isString)
                tempDto.price = price.asString
            else
                return ResponseEntity.status(400).build()
        }
        movieDto = tempDto
        movieService.save(movieDto)

        return ResponseEntity.status(204).build()

    }
}