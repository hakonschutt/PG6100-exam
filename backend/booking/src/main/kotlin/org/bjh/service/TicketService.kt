package org.bjh.service

import org.bjh.repository.TicketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TicketService {
    @Autowired
    private lateinit var ticketRepository : TicketRepository
}