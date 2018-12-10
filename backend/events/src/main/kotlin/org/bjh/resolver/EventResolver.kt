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

@Component
class EventResolver : GraphQLResolver<EventType> {
    @Autowired
    private lateinit var httpService: HttpService

    fun movie(event: EventType): MovieType? {
        // TODO: Create HTTP request
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