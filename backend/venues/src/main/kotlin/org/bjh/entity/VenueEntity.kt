package org.bjh.entity

import org.bjh.entity.RoomEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity(name = "venues")
class VenueEntity(
    @get:Id
    @GeneratedValue
    val id: Long? = null,
    @get:NotBlank
    var name:String? = null,

    @get:NotBlank
    var geoLocation:String? = null,

    @get:NotBlank
    var address:String? = null,

    @get:ElementCollection(fetch = FetchType.EAGER)
    @get:ManyToOne
    var rooms:Set<RoomEntity> = setOf()
)

