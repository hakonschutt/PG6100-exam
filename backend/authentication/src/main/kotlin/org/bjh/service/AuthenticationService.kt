package org.bjh.service

import org.bjh.entity.UserEntity
import org.bjh.repository.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

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

}