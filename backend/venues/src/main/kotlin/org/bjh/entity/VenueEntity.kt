package org.bjh.entity

import org.bjh.entity.RoomEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.math.min

@Entity(name = "venues")
class VenueEntity(
    @get:Id
    @GeneratedValue
    val id: Long? = null,
    @get:NotBlank
    @get:Size(min=1,max=160)
    var name:String? = null,

    @get:NotBlank
    @get:Size(min=5,max=300)
    var geoLocation:String? = null,

    @get:NotBlank
    @get:Size(min=2,max=120)
    var address:String? = null,

    @get:ElementCollection(fetch = FetchType.EAGER)
    @get:ManyToOne(cascade = [CascadeType.ALL])
    var rooms:Set<RoomEntity> = setOf()
)

