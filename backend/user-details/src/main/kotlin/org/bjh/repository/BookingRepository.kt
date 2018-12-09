package org.bjh.repository

import org.bjh.entity.UserDetailEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<UserDetailEntity.BookingDtoTemp,String>