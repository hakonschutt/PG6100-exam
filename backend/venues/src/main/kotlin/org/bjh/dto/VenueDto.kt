package org.bjh.dto

import org.bjh.entity.RoomEntity

class VenueDto(
        var id: String?,
        val geoLocation: String?,
        val address: String?,
        val rooms: Set<RoomDto>,
        var name: String?
        )


