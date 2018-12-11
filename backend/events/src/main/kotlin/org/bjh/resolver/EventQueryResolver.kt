package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.bjh.service.EventService
import org.bjh.type.EventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
/** @author  Kleppa && håkonschutt */
class EventQueryResolver : GraphQLQueryResolver {
    @Autowired
    private lateinit var eventService: EventService

    fun version() = "1.0.0"

    fun allEvents(venue: String?, movie: String?): List<EventType> = when {
        venue.isNullOrEmpty() && !movie.isNullOrEmpty() -> eventService.findAllEventsByMovie(movie!!)
        (!venue.isNullOrEmpty() && movie.isNullOrEmpty()) -> eventService.findAllEventsByVenue(venue!!)
        else -> eventService.findAll()
    }

    fun eventById(eventId: String?): EventType? {
        //todo not
        return null
    }
}
