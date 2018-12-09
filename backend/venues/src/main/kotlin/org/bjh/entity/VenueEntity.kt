package org.bjh.entity

import org.springframework.data.domain.Page
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Entity(name = "venues")
class VenueEntity(
    @get:Id
    @get:GeneratedValue(strategy=GenerationType.IDENTITY)
    var id: Long? = null,
    @get:NotBlank
    @get:Size(min=1,max=160)
    var name:String? = null,

    @get:NotBlank
    @get:Size(min=1,max=300)
    var geoLocation:String? = null,

    @get:NotBlank
    @get:Size(min=1,max=120)
    var address:String? = null,

    @get:ElementCollection(fetch = FetchType.EAGER)
    @get:OneToMany(cascade = [CascadeType.ALL])
    @get:JoinTable(name="venue_room",joinColumns = [JoinColumn(name="room_id",referencedColumnName = "id")])
    var rooms:Set<RoomEntity>
)

