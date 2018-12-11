package org.bjh.type

import java.time.ZonedDateTime
/** @author  Kleppa && håkonschutt */
data class EventType(
        var id: Long,
        var date: ZonedDateTime,
        var movieId: String,
        var venueId: String,
        var roomId: String,
        var rows: Int,
        var columns: Int
)