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
import org.bjh.wrappers.WrappedResponse
/** @author  Kleppa && håkonschutt */

@Component
class EventResolver : GraphQLResolver<EventType> {
    @Autowired
    private lateinit var httpService: HttpService

    fun movie(event:EventType): MovieType? {
        // TODO: Add circuit breaker

        val req = (httpService.getReq("http://localhost:8080/math/divide?x=4&y=2") as WrappedResponse<Any>).data
        println("This is the wrapped response from mathapi ${req}")

        return null
    }

    fun venue(event: EventType): VenueType? {
        // TODO: Create HTTP request
        return null
    }

    fun room(event: EventType): RoomType? {
        // TODO: Create HTTP request
        return null
    }
}