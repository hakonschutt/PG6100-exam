package main.kotlin.org.bjh.dto

import main.kotlin.org.bjh.entity.RoomsEntity

class VenuesDto(
        var id: String?,
        val geoLocation: String?,
        val address: String?,
        val rooms: Set<RoomsEntity>,
        var name: String?
        )


