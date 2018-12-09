package org.bjh.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity(name = "user_detail")
class UserDetailEntity(


        @get:Id
        @get:NotBlank
        @get:Size(min=1,max=255)
        var email: String?,

        @ElementCollection(fetch = FetchType.EAGER)
        @get:OneToMany(cascade = [CascadeType.ALL])
        @get:JoinTable(name="venue_room",joinColumns = [JoinColumn(name="booking_id",referencedColumnName = "id")])
        var purchaseHistory: Set<BookingDtoTemp>?
){
    @Entity(name="booking")
     class BookingDtoTemp(
            @get:Id
            @get:GeneratedValue
            var id: Long? = null,
            @get:Size(min=1,max=180)
            var name: String?
    )

}