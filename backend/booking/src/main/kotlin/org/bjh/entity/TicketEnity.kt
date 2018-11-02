package org.bjh.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author hakonschutt
 */
@Entity(name = "tickets")
class TicketEntity(
        var seat: String? = null,
        var price: Double? = null,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)