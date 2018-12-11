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
            println("########MOVIE###########")
            val req = (httpService.getReq("http://${webAddress.trim()}/api/movies/${event.movieId}")) as WrappedResponse<List<MovieDto>>

            println("###################################")
            println("This is the wrapped response from movie api ${req.data}, ${req.message}")
            println("###################################")

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
        // TODO: Create HTTP request
        try {
            val req = (httpService.getReq("http://localhost:8080/api/venues/${event.venueId}") as WrappedResponse<List<VenueDto>>)
            println("This is the wrapped response from venue api ${req}")
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
            val req = (httpService.getReq("http://localhost:8080/api/venues/${event.venueId}/rooms") as WrappedResponse<List<RoomDto>>)
            println("This is the wrapped response from room api ${req}")
            val data = req.data
            if (data != null) {
                if (data.isNotEmpty()) {
                    val json = Gson().toJson(data, LinkedHashMap::class.java)
                    return EventConverter.transformRoomDtoToType(Gson().fromJson(json, RoomDto::class.java))
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