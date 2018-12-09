package org.bjh.dto

class UserDetailDto(
        var email:String? = null,
        var purchaseHistory : Set<BookingDtoTemp> = setOf()
)