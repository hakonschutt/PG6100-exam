package org.bjh.repository

import org.bjh.entity.RoomEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : PagingAndSortingRepository<RoomEntity, Long> {

}
