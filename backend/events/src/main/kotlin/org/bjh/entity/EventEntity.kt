package org.bjh.entity

import org.hibernate.validator.constraints.Range
import sun.util.calendar.BaseCalendar
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class EventEntity(
        @get:Id @get:GeneratedValue
        var id: Long? = null,
        var date: ZonedDateTime,
        var movieId: Long,
        var venueId: Long,
        var roomId: Long,
        @get:Range(min=1,max=80)
        var rows: Int?,
        @get:Range(min=1,max=80)
        var columns: Int?
)