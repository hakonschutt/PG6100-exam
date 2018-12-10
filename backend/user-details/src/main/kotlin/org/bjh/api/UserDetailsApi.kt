package org.bjh.api

import io.restassured.RestAssured.basePath
import io.swagger.annotations.*
import org.bjh.dto.UserDetailDto
import org.bjh.pagination.PageDto
import org.bjh.service.UserDetailsService
import org.bjh.wrapper.UserDetailWrapper
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@Api("Api for User Details")
@RequestMapping("/api/users")
class UserDetailsApi {
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    private val basePath = "/api/users"
    @ApiOperation("Fetch all users")
    @GetMapping()
    @ApiResponse(code = 200, message = "Returns a list of all the user details")
    fun getAllUserDetails(


            @ApiParam("Offset param to determine what part of the  result table you want back")
            @RequestParam("offset", required = false, defaultValue = "0")
            offset: Int,
            @ApiParam("Limit param to determine what size of the result table you want back")
            @RequestParam("limit", required = false, defaultValue = "20")
            limit: Int
    ): ResponseEntity<WrappedResponse<PageDto<UserDetailDto>>> {
        var validatedOffset = offset
        var validatedLimit = limit
        if (offset < 0) {
            validatedOffset = 0
        }
        if (limit < 1) {
            validatedLimit = 0
        }
        val list = userDetailsService.findAll(validatedOffset, validatedLimit)

        val wrappedResponse = UserDetailWrapper(code = 200, data = list, message = "List of user details").validated()

        return ResponseEntity.ok().body(wrappedResponse)
    }

    @ApiOperation("Fetch a user based on email")
    @GetMapping(path = ["/{id}"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getUser(@ApiParam("The unique id of the User")
                @PathVariable("id") idFromPath: String,
                @ApiParam("loading with history, or not, default is with")
                @RequestParam("withHistory", required = false)
                withHistory: Boolean = true
    ): ResponseEntity<WrappedResponse<UserDetailDto>> {
        val result: ResponseEntity<WrappedResponse<UserDetailDto>>

        val userpg = userDetailsService.findById(idFromPath)
        result = if (userpg!= null) {
            ResponseEntity.status(200)
                    .body(WrappedResponse(
                            code = 200,
                            data = userpg,
                            message = "The single user - details that was requested")
                            .validated())
        } else {
            ResponseEntity.status(404).build()
        }
        return result
    }

    @ApiOperation("create a user")
    @PostMapping(consumes = ["application/json;charset=UTF-8"])
    @ApiResponses(
            ApiResponse(code = 201, message = "The url of newly created user"),
            ApiResponse(code = 400, message = "Bad request")
    )
    fun createUser(@ApiParam("User details data transfer object with atleast email") @RequestBody dto: UserDetailDto): ResponseEntity<WrappedResponse<Unit>> {


        if (dto.email.isNullOrEmpty()) {
            return ResponseEntity.status(400).body(WrappedResponse<Unit>(code = 400, message = "Email is not present").validated())
        }
        val user = userDetailsService.createUser(dto)

        return ResponseEntity.created(URI.create(basePath + "/" + user.email)).body(
                WrappedResponse<Unit>(code = 201, message = "User was created").validated())

    }

    @ApiOperation("Delete a user by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The epost of the user")
            @PathVariable("id")
            epost: String
    ): ResponseEntity<WrappedResponse<Unit>> {


        val deleted = userDetailsService.deleteById(epost)

        if (!deleted)
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(
                            code = 404,
                            message = "'$epost' is not an existing user id.")
                            .validated())

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(
                        code = 204,
                        message = "Deleted user with id: $epost")
                        .validated())
    }

}