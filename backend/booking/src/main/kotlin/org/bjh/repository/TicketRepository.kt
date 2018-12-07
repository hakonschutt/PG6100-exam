package org.bjh.repository

import org.bjh.entity.TicketEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : PagingAndSortingRepository<TicketEntity, Long> {}