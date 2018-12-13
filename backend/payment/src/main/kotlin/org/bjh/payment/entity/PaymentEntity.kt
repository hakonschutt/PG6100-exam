package org.bjh.payment.entity

import javax.persistence.*

@Entity(name="payments")
class PaymentEntity(
//        TODO: Implement this when the user_details table/service has been added
//        @get:ElementCollection(fetch = FetchType.EAGER)
//        @get:ManyToOne(cascade = [CascadeType.ALL])
//        @get:JoinTable(name="user_detail",joinColumns = [(JoinColumn(name = "user", referencedColumnName = "username"))])
        @get:Column(name="user_id")
        var user: String,

        var amount: Double,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)