package org.bjh.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity(name = "user_detail")
class AuthenticationEntity(

        @get:Id
        @get:NotBlank
        @get:Size(min=1,max=255)
        var email: String
)

