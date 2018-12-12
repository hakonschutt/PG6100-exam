package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import com.google.gson.Gson
import org.bjh.component.HttpService
import org.bjh.converter.EventConverter
import org.bjh.dto.MovieDto
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.bjh.type.EventType
import org.bjh.type.MovieType
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/** @author  Kleppa && håkonschutt */

@Component
class EventResolver : GraphQLResolver<EventType> {
    @Autowired
    private lateinit var httpService: HttpService

    @Value("\${fixerWebAddress}")
    private lateinit var webAddress: String

    fun movie(event: EventType): MovieType? {
        try {
            val req = (httpService.getReq("http://${webAddress.trim()}/api/movies/${event.movieId}")) as WrappedResponse<List<MovieDto>>

            val data = req.data
            if (data != null) {
                if (data.isNotEmpty()) {
                    val json = Gson().toJson(data[0], LinkedHashMap::class.java)
                    return EventConverter.transformMovieDtoToType(Gson().fromJson(json, MovieDto::class.java))
                }
            }
            return null
        } catch (e: ClassCastException) {
            return null
        } catch (e: TypeCastException) {
            return null

        }
    }

    fun venue(event: EventType): VenueType? {
        try {
            val req = (httpService.getReq("http://${webAddress.trim()}/api/venues/${event.venueId}") as WrappedResponse<List<VenueDto>>)
            val data = req.data
            if (data != null) {
                if (data.isNotEmpty()) {
                    val json = Gson().toJson(data[0], LinkedHashMap::class.java)
                    return EventConverter.transformVenueDtoToType(Gson().fromJson(json, VenueDto::class.java))
                }
            }
            return null

        } catch (e: ClassCastException) {
            return null
        } catch (e: TypeCastException) {
            return null
        }
    }

    fun room(event: EventType): RoomType? {
        try {
            val req = (httpService.getReq("http://${webAddress.trim()}/api/venues/${event.venueId}/rooms") as WrappedResponse<List<RoomDto>>)
            val data = req.data as ArrayList<LinkedHashMap<String, String>>

            if (data.isNotEmpty()) {

                var desiredDto = data.filter { it.get("id") == event.roomId }.first()
                val jsonTo = Gson().toJson(desiredDto, LinkedHashMap::class.java)
                return EventConverter.transformRoomDtoToType(Gson().fromJson(jsonTo, RoomDto::class.java))


            }

            return null

        } catch (e: ClassCastException) {
            return null
        } catch (e: TypeCastException) {
            return null
        }
    }
}