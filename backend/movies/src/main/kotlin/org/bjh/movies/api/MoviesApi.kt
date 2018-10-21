package org.bjh.movies.api

import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * @author bjornosal
 */
@Api(value = "/movies", description = "Handling of creating and retrieving information about movies.")
@RequestMapping(
        path = ["/movies"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class MoviesApi {

}