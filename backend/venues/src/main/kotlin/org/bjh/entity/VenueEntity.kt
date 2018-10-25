package main.kotlin.org.bjh.entity

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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

    @get:ElementCollection
    var rooms:Set<RoomEntity> = setOf()
)

