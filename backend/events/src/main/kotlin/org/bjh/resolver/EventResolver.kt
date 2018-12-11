package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.bjh.component.HttpService
import org.bjh.type.EventType
import org.bjh.type.MovieType
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.apache.http.client.methods.HttpGet;
import org.bjh.converter.EventConverter
import org.bjh.dto.MovieDto
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.bjh.wrappers.WrappedResponse

/** @author  Kleppa && håkonschutt */

@Component
class EventResolver : GraphQLResolver<EventType> {
    @Autowired
    private lateinit var httpService: HttpService

    fun movie(event: EventType): MovieType? {
        try {
            val req = (httpService.getReq("http://localhost:8080/api/movies/${event.movieId}") as WrappedResponse<List<MovieDto>>).data
            println("This is the wrapped response from movie api ${req}")
            if (req != null) {
                if (req.isNotEmpty()) {
                    return EventConverter.transformMovieDtoToType(req[0])
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
            val req = (httpService.getReq("http://localhost:8080/api/venues/${event.venueId}") as WrappedResponse<List<VenueDto>>).data
            println("This is the wrapped response from venue api ${req}")
            if (req != null) {
                if (req.isNotEmpty()) {
                    return EventConverter.transformVenueDtoToType(req[0])
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
            val req = (httpService.getReq("http://localhost:8080/api/venues/${event.venueId}/rooms") as WrappedResponse<List<RoomDto>>).data
            println("This is the wrapped response from room api ${req}")
            if (req != null) {
                if (req.isNotEmpty()) {
                    val roomDto = req.filter { it.id.equals(event.roomId) }[0]
                    return EventConverter.transformRoomDtoToType(roomDto = roomDto)
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