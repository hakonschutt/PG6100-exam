package org.bjh.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Venue Data Transfer Object")
data class VenueDto(
        @ApiModelProperty("The id of the Venue")
        var id: String?,
        @ApiModelProperty("The geo location of the Venue")
        var geoLocation: String?,
        @ApiModelProperty("The address of the Venue")
        var address: String?,
        @ApiModelProperty("The set of rooms of the Venue")
        var rooms: Set<RoomDto>,
        @ApiModelProperty("The name of the Venue")
        var name: String?
)


