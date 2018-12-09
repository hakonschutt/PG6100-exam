package org.bjh.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity(name="booking")
class BookingDtoTemp(
        @get:Id
        @get:GeneratedValue
        var id: Long? = null,
        @get:Size(min=1,max=180)
        var name: String?,
        @get:Size(min=1,max=255)
        var email: String?
)