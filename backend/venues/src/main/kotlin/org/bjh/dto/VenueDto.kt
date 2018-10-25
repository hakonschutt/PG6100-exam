package main.kotlin.org.bjh.dto

import main.kotlin.org.bjh.entity.RoomEntity

class VenueDto(
        var id: String?,
        val geoLocation: String?,
        val address: String?,
        val rooms: Set<RoomEntity>,
        var name: String?
        )


