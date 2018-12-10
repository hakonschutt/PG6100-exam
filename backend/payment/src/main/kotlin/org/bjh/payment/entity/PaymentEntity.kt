package org.bjh.payment.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name="payment")
class PaymentEntity(

        var userId: Int,
        var price: Double,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)