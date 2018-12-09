package org.bjh.repository

import org.bjh.dto.BookingDtoTemp
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<BookingDtoTemp,Long>