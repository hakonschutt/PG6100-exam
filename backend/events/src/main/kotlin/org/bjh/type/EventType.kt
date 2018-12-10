package org.bjh.type

import java.time.ZonedDateTime
/** @author  Kleppa && håkonschutt */
data class EventType(
        var id: Long,
        var date: ZonedDateTime,
        var movieId: MovieType,
        var venueId: VenueType,
        var roomId: RoomType,
        var rows: Int,
        var columns: Int
)