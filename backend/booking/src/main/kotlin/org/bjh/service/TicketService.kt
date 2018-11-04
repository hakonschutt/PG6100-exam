package org.bjh.service

import org.bjh.converter.TicketConverter
import org.bjh.dto.TicketDto
import org.bjh.entity.TicketEntity
import org.bjh.repository.TicketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TicketService {
    @Autowired
    private lateinit var ticketRepository : TicketRepository

    fun findById(id: Long) : TicketDto {
        val ticket = ticketRepository.findById(id)

        return when {
            ticket.isPresent -> TicketConverter.transform(ticket.get())
            else ->
                return TicketDto(seat = null, price = null, id = null)
        }
    }

    fun updateTicket(dto: TicketDto) : Boolean {
        val id: Long
        try {
            id = dto.id!!.toLong()
        } catch (e: Exception) {
            return false
        }

        val ticketEntity = ticketRepository.save(
            TicketEntity(id = id, price = dto.price, seat = dto.seat)
        )

        return ticketEntity.id != null
    }
}