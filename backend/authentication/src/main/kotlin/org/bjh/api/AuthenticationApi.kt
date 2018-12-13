package org.bjh.api

import io.swagger.annotations.*
import org.bjh.dto.UserDto
import org.bjh.service.AuthenticationService
import org.bjh.wrappers.WrappedResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.security.Principal

@RestController
@Api("Api for authentication")
@RequestMapping()
class AuthenticationApi(
        private val authenticationService: AuthenticationService,
        private val authenticationManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService
) {


    /**
     * From Andrea's repository.
     * @author arcuri82
     */
    @RequestMapping("/user")
    fun user(user: Principal): ResponseEntity<Map<String, Any>> {
        val map = mutableMapOf<String, Any>()
        map["name"] = user.name
        map["roles"] = AuthorityUtils.authorityListToSet((user as Authentication).authorities)
        return ResponseEntity.ok(map)
    }

    @ApiOperation("create a user")
    @PostMapping(path = ["/signUp"],
            consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    @ApiResponses(
            ApiResponse(code = 204, message = "The url of newly created user"),
            ApiResponse(code = 400, message = "Bad request")
    )
    fun createUser(@ApiParam("User info")
                   @RequestBody dto: UserDto): ResponseEntity<WrappedResponse<Unit>> {


        if (dto.username == null || dto.username!!.isEmpty()) {
            return ResponseEntity.status(400)
                    .body(WrappedResponse<Unit>(
                            code = 400,
                            message = "Username is not present")
                            .validated())
        }

        val created = authenticationService.createUser(dto.username!!, dto.password!!, setOf("USER"))

        if (!created)
            return ResponseEntity.status(400)
                    .body(WrappedResponse<Unit>(
                            code = 400,
                            message = "Could not create user")
                            .validated())

        val userDetails = userDetailsService.loadUserByUsername(dto.username)
        val token = UsernamePasswordAuthenticationToken(userDetails, dto.password, userDetails.authorities)

        authenticationManager.authenticate(token)

        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
        }

        return ResponseEntity.status(204).build()
    }


    @PostMapping(path = ["/login"],
            consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun login(@RequestBody dto: UserDto)
            : ResponseEntity<Unit> {

        val userId : String = dto.username!!
        val password : String = dto.password!!

        val userDetails = try{
            userDetailsService.loadUserByUsername(userId)
        } catch (e: UsernameNotFoundException){
            return ResponseEntity.status(400).build()
        }

        val token = UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)

        authenticationManager.authenticate(token)

        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(400).build()
    }
}