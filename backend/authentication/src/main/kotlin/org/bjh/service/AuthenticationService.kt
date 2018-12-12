package org.bjh.service

import org.bjh.converter.AuthenticationConverter
import org.bjh.dto.AuthenticationDto
import org.bjh.entity.AuthenticationEntity
import org.bjh.pagination.HalLink
import org.bjh.pagination.PageDto
import org.bjh.repository.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    fun save(authenticationDto: AuthenticationDto): AuthenticationEntity? {
        if (((authenticationDto.email.isNullOrEmpty()))) {
            return null
        }
        return authenticationRepository.save(AuthenticationEntity(email = authenticationDto.email!!))
    }

    fun findAll(): List<AuthenticationEntity> {
        return authenticationRepository.findAll().toList()
    }


    fun findAll( offset: Int = 0, limit: Int = 20): PageDto<AuthenticationDto> {
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

    fun findById(id: String): AuthenticationDto? {

        val entity = authenticationRepository.findById(id)
        if(entity.isPresent){
            return AuthenticationDto(email = entity.get().email)
        }
        return null

    }

    fun createUser(dto: AuthenticationDto): AuthenticationEntity {
        val entity = authenticationRepository.save(AuthenticationEntity(email = dto.email!!))
        return entity

    }

    fun deleteById(id: String): Boolean {
        if (!authenticationRepository.existsById(id))
            return false

        authenticationRepository.deleteById(id)
        return true
    }

}