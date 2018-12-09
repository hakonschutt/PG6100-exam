package org.bjh.converter

import org.bjh.dto.UserDetailDto
import org.bjh.entity.UserDetailEntity
import org.bjh.pagination.PageDto

class UserDetailsConverter {
    companion object {
        fun transform(userList: List<UserDetailEntity>, withHistory: Boolean): PageDto<UserDetailDto> {
            return PageDto(
                   list = userList.map{ transform(it,withHistory)} as MutableList<UserDetailDto>,
                    totalSize = userList.size
            )
        }
        fun transform(entity: UserDetailEntity, withHistory: Boolean): UserDetailDto {

            return UserDetailDto(
                    email = entity.email,
                    purchaseHistory = entity.purchaseHistory!!
            )

        }

        fun transform(userList: List<UserDetailEntity>, withHistory: Boolean, offset: Int, limit: Int): PageDto<UserDetailDto> {

            val listOfUserDto = userList.map { transform(it, withHistory) }

            return PageDto(
                    list = listOfUserDto as MutableList<UserDetailDto>,
                    rangeMin = offset,
                    rangeMax = offset + listOfUserDto.size - 1,
                    totalSize = listOfUserDto.size
            )
        }

    }
}
