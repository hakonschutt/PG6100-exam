package org.bjh.movies.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.service.MovieService
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


/**
 * @author bjornosal
 */
@Api(value = "/movies", description = "Retrieves movies.")
@RequestMapping(
        path = ["/movies"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class MoviesApi {
    private lateinit var movieService: MovieService

    @ApiOperation("Get all the movies")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)],
            path = ["/"])
    fun getAllMovies(): ResponseEntity<WrappedResponse<List<MovieDto>>> {

        val list = movieService.getAll()

        //TODO: Add ReponseDto
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
                     @PathVariable("id") id: Long): ResponseEntity<WrappedResponse<MovieDto>> {

        //TODO: Add ReponseDto
        return ResponseEntity.ok(
                WrappedResponse(
                        code = 200,
                        data = movieService.getById(id))
                        .validated())

    }

    //TODO: Figure out if I can run /{title} just as with id.
    @ApiOperation("Get a movie by title")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)],
            path = ["/{title}"])
    fun getMovieByTitle(@ApiParam("Title of a movie")
                        @PathVariable("title") title: String): ResponseEntity<WrappedResponse<List<MovieDto>>> {
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
                    @RequestBody dto: MovieDto): ResponseEntity<Long> {

        //TODO: Needs an implementation of rules of when to create, and not.
        return ResponseEntity.ok(1L)
    }


    @ApiOperation("Delete a movie by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The id of the movie")
            @PathVariable("id")
            movieId: String
    ): ResponseEntity<WrappedResponse<Unit>> {

        //TODO: Needs an implementation of rules of when to delete, and not delete.
        return ResponseEntity.status(404).body(WrappedResponse<Unit>(code = 404).validated())

        /*return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(code = 204).validated())*/
    }

    @ApiOperation("Update a specific movie")
    @PatchMapping(path = ["/{id}"])
    fun updateMovie(
            @ApiParam("The id the of the movie")
            @PathVariable("id")
            id: String?,
            @ApiParam("Fields that will be updated/added")
            @RequestBody
            jsonPatch: String): ResponseEntity<WrappedResponse<Unit>>{
        //TODO: Needs an implementation of rules of when to update, and not update.

        return ResponseEntity.status(404).body(WrappedResponse<Unit>(code = 404).validated())
    }


}