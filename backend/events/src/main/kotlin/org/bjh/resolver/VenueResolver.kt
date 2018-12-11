package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import com.google.gson.Gson
import org.bjh.component.HttpService
import org.bjh.converter.EventConverter
import org.bjh.dto.RoomDto
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/** @author  Kleppa && håkonschutt */

@Component
class VenueResolver : GraphQLResolver<VenueType> {
    @Autowired
    private lateinit var httpService: HttpService
    @Value("\${fixerWebAddress}")
    private lateinit var webAddress: String

    fun rooms(venue: VenueType): Set<RoomType>? {
        try {
            val req = (httpService.getReq("http://${webAddress.trim()}/api/venues/${venue.id}/rooms") as WrappedResponse<List<RoomDto>>)
            val data = req.data as ArrayList<LinkedHashMap<String, String>>

            if (data.isNotEmpty()) {

                val jsonTo = data.map { Gson().toJson(it, LinkedHashMap::class.java) }

                val dtos = jsonTo.map { Gson().fromJson(it, RoomDto::class.java) }
                dtos.forEach{println(dtos)}
                return dtos.map { EventConverter.transformRoomDtoToType(it) }.toSet()
            }
            return null
        } catch (e: ClassCastException) {
            return null
        } catch (e: TypeCastException) {
            return null
        }
    }
}