package org.bjh.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author hakonschutt
 */
@ApiModel(description = "Booking information")
data class BookingDto(
    // TODO: Added a relational mapping to user and event Entity when the DTO's are split to a separate microservice
    // var user: User? = null
    @ApiModelProperty("The associated user for the given booking")
    var user: Long? = null,

    // var event: Event? = null
    @ApiModelProperty("The associated event for the given booking")
    var event: Long? = null,

    @ApiModelProperty("The associated tickets for the given booking")
    var tickets: Set<TicketDto>,

    @ApiModelProperty("The id of the booking")
    var id: Long? = null
)
