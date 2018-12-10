package org.bjh.serivce

import org.bjh.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/** @author  Kleppa && håkonschutt */
@Service
class EventService{
    @Autowired
    private lateinit var eventRepository: EventRepository
}