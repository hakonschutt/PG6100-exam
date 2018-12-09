package org.bjh.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author hakonschutt
 */
@ApiModel(description = "Ticket information")
data class TicketDto (
    @ApiModelProperty("The seat for the ticket")
    var seat: String? = null,

    @ApiModelProperty("The price of the ticket")
    var price: Double? = null,

    @ApiModelProperty("The id of the ticket")
    var id: Long? = null
)