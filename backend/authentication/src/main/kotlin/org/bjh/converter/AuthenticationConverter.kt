package org.bjh.converter

import org.bjh.dto.AuthenticationDto
import org.bjh.entity.AuthenticationEntity
import org.bjh.pagination.PageDto

class AuthenticationConverter {
    companion object {
        fun transform(dto: List<AuthenticationEntity>): List<AuthenticationDto> {
            return dto.map{ transform(it)}
        }
        fun transform(dto: AuthenticationEntity): AuthenticationDto {
            return AuthenticationDto(email = dto.email)
        }

        fun transform(userList: List<AuthenticationEntity>, offset: Int, limit: Int): PageDto<AuthenticationDto> {
            val listOfAuthenticationDto = userList.map { transform(it) }

            return PageDto(
                    list = listOfAuthenticationDto as MutableList<AuthenticationDto>,
                    rangeMin = offset,
                    rangeMax = offset + listOfAuthenticationDto.size - 1,
                    totalSize = listOfAuthenticationDto.size
            )
        }
    }


}
