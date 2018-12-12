package org.bjh.type

/** @author  Kleppa && håkonschutt */
data class EventInputType(
        var date: String,
        var movieId: String,
        var venueId: String,
        var roomId: String,
        var rows: Int,
        var columns: Int
)