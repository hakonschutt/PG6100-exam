package org.bjh.repository

import org.bjh.entity.RoomEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : CrudRepository<RoomEntity, Long> {

     @Query("SELECT r.* FROM rooms r  JOIN venues_rooms as vr" +
             " WHERE vr.venues_id  = :id ", nativeQuery = true)
     fun findAllByVenueId(
             @Param("id") id: Long
     ): Set<RoomEntity>

}
