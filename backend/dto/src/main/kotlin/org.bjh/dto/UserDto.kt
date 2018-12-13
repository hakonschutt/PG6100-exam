package org.bjh.dto

import javax.validation.constraints.NotBlank

class UserDto(
        @get:NotBlank
        var username : String ? = null,

        @get:NotBlank
        var password: String? = null
)