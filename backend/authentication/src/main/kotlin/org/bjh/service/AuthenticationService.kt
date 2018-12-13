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

   /* fun save(authenticationDto: UserDto): UserEntity? {
        if (((authenticationDto.username.isNullOrEmpty()))) {
            return null
        }
        return authenticationRepository.save(UserEntity(username = authenticationDto.username!!, password = ""))
    }
*/
    fun findAll(): List<UserEntity> {
        return authenticationRepository.findAll().toList()
    }


    fun findAll( offset: Int = 0, limit: Int = 20): PageDto<UserDto> {
        val userList = authenticationRepository.findAll(offset, limit)

        val page = AuthenticationConverter.transform(userList = userList ,offset = offset, limit = limit)

        val builder = UriComponentsBuilder
                .fromPath("/users")
                .queryParam("limit", limit)

        page._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )
        //Hal link part is modified from Andreas code
        if (!userList.isEmpty() && offset > 0) {
            page.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }

        if (offset + limit < authenticationRepository.count()) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }
        return page


    }

    fun findById(id: String): UserDto? {

        val entity = authenticationRepository.findById(id)
        if(entity.isPresent){
            return UserDto(username = entity.get().username)
        }
        return null

    }


    fun createUser(dto: UserDto): Boolean {
        try {
            val hash = passwordEncoder.encode(dto.password)

            if (authenticationRepository.existsById(dto.username)) {
                return false
            }

            val user = UserEntity(dto.username, hash)

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