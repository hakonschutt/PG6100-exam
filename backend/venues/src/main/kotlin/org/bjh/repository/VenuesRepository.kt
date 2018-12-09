package org.bjh.repository

import org.bjh.entity.VenueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
interface VenuesRepository : JpaRepository<VenueEntity, Long>{

    @Query("SELECT * FROM venues OFFSET :offset LIMIT :limit", nativeQuery = true)
     fun findAll(
            @Param ("offset") offset:Int=0,
            @Param ("limit")limit:Int=20)
            :List<VenueEntity>

    @Query("SELECT v FROM venues v WHERE v.id = :id")
    fun findAllById( @Param("id")id:Long ):List<VenueEntity>
}
