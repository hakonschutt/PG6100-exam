package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.bjh.serivce.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EventQueryResolver : GraphQLQueryResolver {
    @Autowired
    private lateinit var eventService: EventService
    fun version() = "1.0.0"
    fun allEvents() {}
    fun allEventsForVenue() {}
    fun allEventsForMovies() {}
    fun eventById() {}
}