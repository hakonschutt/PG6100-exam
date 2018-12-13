package org.bjh.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity(name = "users")
class UserEntity(
//Taken from Andrea's repository
        @get:Id
        @get:NotBlank
        var username: String,

        @get:NotBlank
        var password: String?

)

