package org.bjh.dto

import org.bjh.entity.UserDetailEntity

class UserDetailDto(
        var email:String? = null,
        var purchaseHistory : Set<UserDetailEntity.BookingDtoTemp> = setOf()
)