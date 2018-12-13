package org.bjh.dto

import javax.validation.constraints.NotBlank

class UserDto(
        @get:NotBlank
        var username : String,

        @get:NotBlank
        var password: String? = null
)