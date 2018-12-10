package org.bjh.payment.entity

import javax.persistence.*

@Entity(name="payment")
class PaymentEntity(

        @get:ElementCollection(fetch = FetchType.EAGER)
        @get:ManyToOne(cascade = [CascadeType.ALL])
        @get:JoinTable(name="user_detail",joinColumns = [(JoinColumn(name = "user", referencedColumnName = "email"))])
        var user: String,

        var price: Double,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)