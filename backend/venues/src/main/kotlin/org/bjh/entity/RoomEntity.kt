package org.bjh.entity

import org.hibernate.validator.constraints.Range
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity(name = "rooms")
class RoomEntity(
        @get:Id
        @get:GeneratedValue
        var id: Long? = null,
        @get:Size(min=1,max=180)
        var name: String?,
        @get:Range(min=1,max=80)
        var rows: Int?,
        @get:Range(min=1,max=80)
        var columns: Int?) {

}
