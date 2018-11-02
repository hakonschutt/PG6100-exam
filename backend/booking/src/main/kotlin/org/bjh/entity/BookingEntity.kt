package org.bjh.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "bookings")
class BookingEntity(
        // TODO: Added a relational mapping to user and event Entity when the DTO's are split to a separate microservice
        // @get:ManyToOne
        // var user: User? = null
        var user: Long? = null,

        // @get:ManyToOne
        // var event: Event? = null
        var event: Long? = null,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
){}