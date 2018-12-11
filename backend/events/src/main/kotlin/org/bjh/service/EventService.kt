package org.bjh.service

import org.bjh.repository.EventRepository
import org.bjh.type.EventType
import org.bjh.type.MovieType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

/** @author  Kleppa && håkonschutt */
@Service
class EventService{
    @Autowired
    private lateinit var eventRepository: EventRepository

    fun findAll(): List<EventType> {
        val listOfEvents = eventRepository.findAll()
        return listOf(EventType(
                    id = 1,
                    date = ZonedDateTime.now(),
                    movieId = "1",
                    venueId = "2",
                    roomId = "2",
                    rows = 10,
                    columns = 10
            ))
    }

    fun findAllEventsByMovie(movie: String?): List<EventType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return emptyList()
    }

    fun findAllEventsByVenue(venue: String?): List<EventType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return emptyList()
    }


}