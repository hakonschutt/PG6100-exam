package org.bjh.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Information about a payment")
data class PaymentDto (
        @ApiModelProperty("id of the payment")
        var id: String? = null,
        @ApiModelProperty("The user completing the payment")
        var user: String,
        @ApiModelProperty("Payment auth token from Stripe")
        var paymentAuthorizationToken: String? = null,
        @ApiModelProperty("The amount of the payment transaction")
        var amount: String? = null
)