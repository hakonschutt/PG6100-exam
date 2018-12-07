package org.bjh.entity

import javax.persistence.*

/**
 * @author hakonschutt
 */
@Entity(name = "bookings")
class BookingEntity(
        // TODO: Added a relational mapping to user and event Entity when the DTO's are split to a separate microservice
        // @get:ManyToOne
        // var user: User? = null
        var user: Long? = null,

        // @get:ManyToOne
        // var event: Event? = null
        var event: Long? = null,

        @get:ElementCollection(fetch = FetchType.LAZY)
        @get:OneToMany(cascade = [CascadeType.ALL])
        @get:JoinTable(name="booking_tickets", joinColumns = [JoinColumn(name="ticket_id",referencedColumnName = "id")])
        var tickets: Set<TicketEntity>,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)