package org.bjh.converter

import org.bjh.dto.UserDetailDto
import org.bjh.entity.UserDetailEntity
import org.bjh.pagination.PageDto

class UserDetailsConverter {
    companion object {
        fun transform(dto: List<UserDetailEntity>): List<UserDetailDto> {
            return dto.map{ transform(it)}
        }
        fun transform(dto:UserDetailEntity):UserDetailDto{
            return UserDetailDto(email = dto.email)
        }

        fun transform(userList: List<UserDetailEntity>, offset: Int, limit: Int): PageDto<UserDetailDto> {
            val listOfUserDto = userList.map { transform(it) }

            return PageDto(
                    list = listOfUserDto as MutableList<UserDetailDto>,
                    rangeMin = offset,
                    rangeMax = offset + listOfUserDto.size - 1,
                    totalSize = listOfUserDto.size
            )
        }
    }


}
