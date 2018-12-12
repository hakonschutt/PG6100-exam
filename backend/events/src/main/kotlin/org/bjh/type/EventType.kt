package org.bjh.type

/** @author  Kleppa && håkonschutt */
data class EventType(
        var id: Long,
        var date: String,
        var movieId: String,
        var venueId: String,
        var roomId: String,
        var rows: Int,
        var columns: Int
)