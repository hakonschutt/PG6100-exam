package org.bjh.service

import org.bjh.converter.AuthenticationConverter
import org.bjh.dto.UserDto
import org.bjh.entity.UserEntity
import org.bjh.pagination.HalLink
import org.bjh.pagination.PageDto
import org.bjh.repository.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class AuthenticationService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    fun findById(id: String): UserDto? {

        val entity = authenticationRepository.findById(id)
        if(entity.isPresent){
            return UserDto(username = entity.get().username)
        }
        return null

    }


    fun createUser(username: String, password: String, roles: Set<String> = setOf()): Boolean {
        try {
            val hash = passwordEncoder.encode(password)

            if (authenticationRepository.existsById(username)) {
                return false
            }

            val user = UserEntity(username, hash, roles.map{"ROLE_$it"}.toSet())

            authenticationRepository.save(user)

            return true
        } catch (e: Exception){
            return false
        }
    }

    fun deleteById(id: String): Boolean {
        if (!authenticationRepository.existsById(id))
            return false

        authenticationRepository.deleteById(id)
        return true
    }

}