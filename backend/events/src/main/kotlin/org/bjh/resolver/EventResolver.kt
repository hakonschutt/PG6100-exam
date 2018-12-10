package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.bjh.type.EventType
import org.bjh.type.MovieType
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.springframework.stereotype.Component

@Component
class EventResolver : GraphQLResolver<EventType> {
    fun movie(event : EventType): MovieType? {
        // TODO: Create HTTP request
        return null
    }

    fun venue(event : EventType): VenueType? {
        // TODO: Create HTTP request
        return null
    }

    fun room(event : EventType): RoomType? {
        // TODO: Create HTTP request
        return null
    }
}