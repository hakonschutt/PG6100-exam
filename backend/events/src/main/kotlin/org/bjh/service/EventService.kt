package org.bjh.service

import org.bjh.repository.EventRepository
import org.bjh.type.EventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/** @author  Kleppa && håkonschutt */
@Service
class EventService{
    @Autowired
    private lateinit var eventRepository: EventRepository

    fun findAll(): List<EventType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val listOfEvents = eventRepository.findAll()














        return emptyList()
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