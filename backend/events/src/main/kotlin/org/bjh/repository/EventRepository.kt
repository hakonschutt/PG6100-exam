package org.bjh.repository

import org.bjh.entity.EventEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
/** @author  Kleppa && håkonschutt */

@Repository
interface EventRepository:CrudRepository<EventEntity,Long> {

    @Query("SELECT * FROM events WHERE movieId = :movieId", nativeQuery = true)
    fun findAllEventsByMovie(@Param("movieId") movieId: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE venueId = :venueId", nativeQuery = true)
    fun findAllEventsByVenue(@Param("venueId") venueId: String): List<EventEntity>


}