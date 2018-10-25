package org.bjh.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "rooms")
class RoomEntity(
        @get:Id
        @get:GeneratedValue
        var id: Long? = null,
        val name: String,
        val rows: Int,
        val columns: Int) {

}
