package org.bjh.type

import java.time.ZonedDateTime
/** @author  Kleppa && håkonschutt */
data class EventInputType(
        var date: String,
        var movieId: String,
        var venueId: String,
        var roomId: String,
        var rows: Int,
        var columns: Int
)