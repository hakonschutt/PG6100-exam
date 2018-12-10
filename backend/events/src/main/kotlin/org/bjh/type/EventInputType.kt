package org.bjh.type

import java.time.ZonedDateTime
/** @author  Kleppa && håkonschutt */
data class EventInputType(
        var data: ZonedDateTime,
        var movieId: Long,
        var venueId: Long,
        var roomId: Long,
        var rows: Int,
        var columns: Int
)