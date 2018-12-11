package org.bjh.entity

import org.hibernate.validator.constraints.Range
import sun.util.calendar.BaseCalendar
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name="events")
/** @author  Kleppa && hakonschutt */
class EventEntity(
        @get:Id @get:GeneratedValue
        var id: Long? = null,
        var date: String,
        var movieId: String,
        var venueId: String,
        var roomId: String,
        @get:Range(min=1,max=80)
        var rows: Int?,
        @get:Range(min=1,max=80)
        var columns: Int?
)

