package org.bjh.dto
data class VenueDto(
        var id: String?,
        var geoLocation: String?,
        var address: String?,
        var rooms: Set<RoomDto>,
        var name: String?
)


