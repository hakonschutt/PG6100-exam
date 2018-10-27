package org.bjh.dto
class VenueDto(
        val id: String?,
        var geoLocation: String?,
        var address: String?,
        var rooms: Set<RoomDto>,
        var name: String?
)


