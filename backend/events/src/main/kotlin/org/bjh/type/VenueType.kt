package org.bjh.type
/** @author  Kleppa && håkonschutt */
data class VenueType(
       var id: String,
        var geoLocation: String,
        var address: String,
        var rooms: Set<RoomType>,
        var name: String
)