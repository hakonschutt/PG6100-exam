package org.bjh.repository

import org.bjh.entity.BookingEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<BookingEntity, Long> {}