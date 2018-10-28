package org.bjh.movies.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.service.MovieService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


/**
 * @author bjornosal
 */
@Api(value = "/movies", description = "Retrieves movies.")
@RequestMapping(
        path = ["/api/movies"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class MoviesApi {

    @Autowired
    private lateinit var movieService: MovieService

    private val JSON_PRIMITIVE_TYPE_NUMBER = "number"
    private val JSON_PRIMITIVE_TYPE_STRING = "string"
    private val JSON_PRIMITIVE_TYPE_BOOLEAN = "boolean"

    //TODO: Add ReponseDto to responses?

    //TODO: Pagination with infinity-scroll. How to?
    @ApiOperation("Get all the movies")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getAllMovies(): ResponseEntity<WrappedResponse<List<MovieDto>>> {

        val list = movieService.getAll()

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
                     movieId: String): ResponseEntity<WrappedResponse<MovieDto>> {

        val id: Long
        try {
            id = movieId.toLong()
        } catch (ne: NumberFormatException) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<MovieDto>(
                            code = 400,
                            message = "'$movieId' is not a valid movie id.")
                            .validated())
        }

        val movieDto = movieService.getById(id)
        if (movieDto.id == null)
            return ResponseEntity.status(404).body(WrappedResponse<MovieDto>(code = 404).validated())

        return ResponseEntity.ok(
                WrappedResponse(
                        code = 200,
                        data = movieDto)
                        .validated())

    }

    //TODO: Figure out if I can run /{title} just as with id.
    @ApiOperation("Get a movie by title")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)],
            path = ["/{title}"])
    fun getMovieByTitle(@ApiParam("Title of a movie")
                        @PathVariable("title") title: String): ResponseEntity<WrappedResponse<List<MovieDto>>> {

        return ResponseEntity.status(400).body(
                WrappedResponse<List<MovieDto>>(code = 400, message = "Can't get movie by title.")
                        .validated())

        return ResponseEntity.ok(
                WrappedResponse(
                        code = 200,
                        data = movieService.getAllByTitle(title))
                        .validated())
    }

    @ApiOperation("Create a movie")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)],
            path = ["/"])
    fun createMovie(@ApiParam("Information for new movie")
                    @RequestBody movieDto: MovieDto): ResponseEntity<WrappedResponse<Unit>> {

        val created = movieService.createMovie(movieDto)

        if (!created)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(code = 400, message = "Unable to create movie.")
                            .validated())

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(code = 204, message = "Movie was created.")
                        .validated())
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
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "'$movieId' is not an existing movie id.")
                            .validated())

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(
                        code = 204,
                        message = "Deleted movie with id: $movieId")
                        .validated())
    }

    @ApiOperation("Update a specific movie")
    @PatchMapping(path = ["/{id}"])
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

        val movieDto = movieService.getById(id)

        if (movieDto.id == null)
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(
                            code = 404,
                            message = "There was no movie with id $id")
                            .validated())

        val parser = JsonParser()
        val movieObject : JsonObject
        try {
            movieObject  = parser.parse(fieldsToPatch).asJsonObject
        } catch (jse: JsonSyntaxException) {
            return ResponseEntity.status(400).build()
        }

        if (movieObject.has("id")) {
            return ResponseEntity.status(409).build()
        }

        val tempDto = movieDto.copy()

        if(movieObject.has("title")) {
            val title = movieObject.get("title")
            if(title.isJsonNull)
                tempDto.title = null
            else if (title.isJsonPrimitive && title.asJsonPrimitive.isString)
                tempDto.title = title.asString
            else
                return ResponseEntity.status(400).build()
        }

        //TODO: try to implement delegate properties

        return ResponseEntity.status(404).body(WrappedResponse<Unit>(code = 404).validated())
    }


    //TODO: Implement a generic setter for the values
  /*  fun setPrimitiveValueFromJson(tempDto: MovieDto, movieObject: JsonObject, field: String, type: String): {
        if(movieObject.has(field)) {
            val title = movieObject.get(field)


            if(title.isJsonNull)
                tempDto::class.memberProperties
                        .filter { it.toString() == field }
                        .first()
            else if (title.isJsonPrimitive)
                if(type == JSON_PRIMITIVE_TYPE_STRING)
                    tempDto. = title.asString

                else
                return ResponseEntity.status(400).build()
        }
    }*/



}