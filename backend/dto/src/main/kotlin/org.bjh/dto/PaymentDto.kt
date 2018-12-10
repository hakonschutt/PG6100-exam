package org.bjh.dto

data class PaymentDto (
    var id: String? = null,
    var user: String,
    var paymentAuthorizationToken: String? = null,
    var price: String? = null
)