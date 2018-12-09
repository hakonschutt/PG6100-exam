package org.bjh.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author hakonschutt
 */
@ApiModel(description = "Booking information")
data class BookingDto(
    @ApiModelProperty("The associated user for the given booking")
    var user: Long? = null,

    @ApiModelProperty("The associated event for the given booking")
    var event: Long? = null,

    @ApiModelProperty("The associated tickets for the given booking")
    var tickets: Set<TicketDto>,

    @ApiModelProperty("The id of the booking")
    var id: Long? = null
)
