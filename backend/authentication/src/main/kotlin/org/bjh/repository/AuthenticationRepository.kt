package org.bjh.repository

import org.bjh.entity.AuthenticationEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthenticationRepository : CrudRepository<AuthenticationEntity, String> {

    @Query("SELECT * FROM authentication OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
            @Param("offset") offset:Int=0,
            @Param ("limit")limit:Int=20)
            :List<AuthenticationEntity>
    @Query("SELECT * FROM authentication WHERE email = :userId", nativeQuery = true)
    override fun findById(
            @Param("userId") userId: String
    ) : Optional<AuthenticationEntity>
}