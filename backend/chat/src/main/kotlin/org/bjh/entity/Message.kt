package org.bjh.entity

import javax.persistence.*


@Entity(name = "message")

class Message(

        @get:Id
        @get:GeneratedValue(strategy=GenerationType.IDENTITY)

        var id: Long? = null,
        @get:ElementCollection(fetch = FetchType.EAGER)
        var msg: List<String> = listOf(),
        @get:Column var toUser: Long? = null,
        @Column var fromUser: Long? = null
)