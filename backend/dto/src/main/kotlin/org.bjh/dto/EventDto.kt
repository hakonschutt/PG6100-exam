package org.bjh.dto

import java.time.ZonedDateTime
import java.util.*

data class EventDto(
        var id: Long? = null,
        var date: ZonedDateTime,
        var movieId: Long,
        var venueId: Long,
        var roomId: Long,
        var rows: Int?,
        var columns: Int?
)