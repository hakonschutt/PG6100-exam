package org.bjh.service

import org.bjh.converter.UserDetailsConverter
import org.bjh.dto.UserDetailDto
import org.bjh.entity.UserDetailEntity
import org.bjh.pagination.HalLink
import org.bjh.pagination.PageDto
import org.bjh.repository.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class UserDetailsService {
    @Autowired
    private lateinit var userDetailsRepository: UserDetailsRepository

    fun save(userDetailDto: UserDetailDto): UserDetailEntity {
        if (((userDetailDto.email.isNullOrEmpty()))) {
            return UserDetailEntity(email = null, purchaseHistory = setOf())
        }
        return userDetailsRepository.save(UserDetailEntity(email = userDetailDto.email, purchaseHistory = userDetailDto.purchaseHistory))
    }

    fun findAll(withHistory: Boolean): List<UserDetailDto> {
        return userDetailsRepository.findAll().toList().map { UserDetailsConverter.transform(it, withHistory = withHistory) }
    }

    fun findById(id: String): UserDetailEntity? {
        return userDetailsRepository.findById(id).orElse(null)


    }

    fun findAll(withHistory: Boolean, offset: Int = 0, limit: Int = 20): PageDto<UserDetailDto> {
        val userList = userDetailsRepository.findAll(offset, limit)

        val page = UserDetailsConverter.transform(userList = userList, withHistory = withHistory, offset = offset, limit = limit)

        var builder = UriComponentsBuilder
                .fromPath("/venues")
                .queryParam("withHistory", withHistory)
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

        if (offset + limit < userDetailsRepository.count()) {
            page.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }
        return page


    }

    fun findAllById(id: Long, withHistory: Boolean): PageDto<UserDetailDto> {
        val dto = userDetailsRepository.findAllById(id)

        return UserDetailsConverter.transform(dto, withHistory)
    }

    fun createUser(dto: UserDetailDto): UserDetailEntity {
        val entity = userDetailsRepository.save(UserDetailEntity(email = dto.email, purchaseHistory = dto.purchaseHistory))
        return entity

    }

    fun deleteById(id: String): Boolean {
        if (!userDetailsRepository.existsById(id))
            return false

        userDetailsRepository.deleteById(id)
        return true
    }

}