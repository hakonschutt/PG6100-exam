package org.bjh.converter

import org.bjh.dto.UserDto
import org.bjh.entity.UserEntity
import org.bjh.pagination.PageDto

class AuthenticationConverter {
    //TODO: Currently not in use.
   /* companion object {
        fun transform(dto: List<UserEntity>): List<UserDto> {
            return dto.map{ transform(it)}
        }
        fun transform(entity: UserEntity): UserDto {
            return UserDto(
                    username = entity.username,
                    password = entity.password
            )
        }

        fun transform(userList: List<UserEntity>, offset: Int, limit: Int): PageDto<UserDto> {
            val listOfAuthenticationDto = userList.map { transform(it) }

            return PageDto(
                    list = listOfAuthenticationDto as MutableList<UserDto>,
                    rangeMin = offset,
                    rangeMax = offset + listOfAuthenticationDto.size - 1,
                    totalSize = listOfAuthenticationDto.size
            )
        }
    }*/


}
