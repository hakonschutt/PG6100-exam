package org.bjh.dto

import java.util.*

data class EventDto(
        var id: Long? = null,
        var date: String,
        var movieId: Long,
        var venueId: Long,
        var roomId: Long,
        var rows: Int?,
        var columns: Int?
)