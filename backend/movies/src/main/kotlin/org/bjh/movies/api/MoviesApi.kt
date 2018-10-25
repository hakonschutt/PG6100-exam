package org.bjh.movies.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.service.MovieService
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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


}