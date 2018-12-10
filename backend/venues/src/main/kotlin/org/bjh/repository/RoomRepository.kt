package org.bjh.repository

import org.bjh.entity.RoomEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : CrudRepository<RoomEntity, Long> {

     @Query("SELECT v.rooms FROM venues v WHERE v.id  = :id", nativeQuery = true)
     fun findAllByVenueId(
             @Param("id") id: String
     ): Set<RoomEntity>

}
