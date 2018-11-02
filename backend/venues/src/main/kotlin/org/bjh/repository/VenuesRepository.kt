package org.bjh.repository

import org.bjh.entity.VenueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
interface VenuesRepository : JpaRepository<VenueEntity, Long>{
    @Query("select * from venues offset :offset limit :limit")
     fun findAll(@Param ("offset") offset:Int,@Param ("limit")limit:Int):List<VenueEntity>

    @Query("select * from venues where id=:id")
    fun findAllById(@Param("id")id:Long):List<VenueEntity>
}
