package org.bjh.service

import org.bjh.converter.EventConverter
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
        val listOfEvents = eventRepository.findAll().toList()
        return EventConverter.transformEventEntityToType(listOfEvents)
    }

    fun findAllEventsByMovie(movie: String): List<EventType> {
        val listOfEventsByMovie = eventRepository.findAllEventsByMovie(movie)
        return EventConverter.transformEventEntityToType(listOfEventsByMovie)
    }

    fun findAllEventsByVenue(venue: String): List<EventType> {
        val listOfEventsByVenue = eventRepository.findAllEventsByVenue(venue)
        return EventConverter.transformEventEntityToType(listOfEventsByVenue)
    }


}