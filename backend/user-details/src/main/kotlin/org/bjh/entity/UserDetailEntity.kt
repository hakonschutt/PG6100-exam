package org.bjh.entity

import org.bjh.dto.BookingDtoTemp
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity(name = "user_detail")
class UserDetailEntity(


        @get:Id
        @get:NotBlank
        @get:Size(min=1,max=255)
        var email: String?,
        @ElementCollection(fetch = FetchType.EAGER)
        @get:OneToMany(cascade = [CascadeType.ALL])
        var purchaseHistory: Set<BookingDtoTemp>? = setOf()
)

