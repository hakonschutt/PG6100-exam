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
import org.springframework.stereotype.Component

/** @author  Kleppa && håkonschutt */

@Component
class VenueResolver : GraphQLResolver<VenueType> {
    @Autowired
    private lateinit var httpService: HttpService

    fun rooms(venue: VenueType): Set<RoomType>? {
        try {
            val req = (httpService.getReq("http://localhost:8080/api/venues/${venue.id}/rooms") as WrappedResponse<List<RoomDto>>)
            val data = req.data
            if (data != null) {
                if (data.isNotEmpty()) {
                    return data.map{ Gson().toJson(it, LinkedHashMap::class.java) }.map{ EventConverter.transformRoomDtoToType(Gson().fromJson(it, RoomDto::class.java))}.toSet()
                }
            }
            return null
        } catch (e: ClassCastException) {
            return null
        } catch (e: TypeCastException) {
            return null
        }
    }
}