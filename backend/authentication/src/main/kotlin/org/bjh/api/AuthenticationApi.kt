package org.bjh.api

import io.swagger.annotations.*
import org.bjh.dto.UserDto
import org.bjh.pagination.PageDto
import org.bjh.service.AuthenticationService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@Api("Api for User Details")
@RequestMapping("/api/users")
class AuthenticationApi {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

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
    ): ResponseEntity<WrappedResponse<PageDto<UserDto>>> {

        var validatedOffset = offset
        var validatedLimit = limit

        if (offset < 0) {
            validatedOffset = 0
        }

        if (limit < 1) {
            validatedLimit = 0
        }

        val list = authenticationService.findAll(validatedOffset, validatedLimit)

        return ResponseEntity.ok()
                .body(WrappedResponse(code = 200,
                        data = list,
                        message = "List of user details")
                        .validated())
    }

    @ApiOperation("Fetch a user based on username")
    @GetMapping(path = ["/{id}"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getUser(@ApiParam("The unique id of the User")
                @PathVariable("id") idFromPath: String,

                @ApiParam("loading with history, or not, default is with")
                @RequestParam("withHistory", required = false)
                withHistory: Boolean = true
    ): ResponseEntity<WrappedResponse<UserDto>> {
        val result: ResponseEntity<WrappedResponse<UserDto>>

        val userpg = authenticationService.findById(idFromPath)
        result = if (userpg != null) {
            ResponseEntity.status(200)
                    .body(WrappedResponse(
                            code = 200,
                            data = userpg,
                            message = "The single user - details that was requested")
                            .validated())
        } else {
            ResponseEntity.status(404)
                    .body(WrappedResponse<UserDto>(
                            code = 404,
                            message = "Unable to find the user with the id $idFromPath")
                            .validated())
        }
        return result
    }

    @ApiOperation("create a user")
    @PostMapping(consumes = ["application/json;charset=UTF-8"])
    @ApiResponses(
            ApiResponse(code = 201, message = "The url of newly created user"),
            ApiResponse(code = 400, message = "Bad request")
    )
    fun createUser(@ApiParam("User details data transfer object with atleast username") @RequestBody dto: UserDto): ResponseEntity<WrappedResponse<Unit>> {


        if (dto.username.isNullOrEmpty()) {
            return ResponseEntity.status(400)
                    .body(WrappedResponse<Unit>(
                            code = 400,
                            message = "Email is not present")
                            .validated())
        }

        val user = authenticationService.createUser(dto)

        return ResponseEntity.created(URI.create(basePath + "/" + user.email)).body(
                WrappedResponse<Unit>(code = 201, message = "User was created").validated())

    }

    @ApiOperation("Delete a user by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The username of the user")
            @PathVariable("id")
            email: String
    ): ResponseEntity<WrappedResponse<Unit>> {


        val deleted = authenticationService.deleteById(email)

        if (!deleted)
            return ResponseEntity.status(404).body(
                    WrappedResponse<Unit>(
                            code = 404,
                            message = "'$email' is not an existing user id.")
                            .validated())

        return ResponseEntity.status(204).body(
                WrappedResponse<Unit>(
                        code = 204,
                        message = "Deleted user with id: $email")
                        .validated())
    }

}