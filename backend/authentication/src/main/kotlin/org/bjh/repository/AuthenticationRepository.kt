package org.bjh.repository

import org.bjh.entity.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthenticationRepository : CrudRepository<UserEntity, String> {
/*
    @Query("SELECT * FROM authentication OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
            @Param("offset") offset:Int=0,
            @Param ("limit")limit:Int=20)
            :List<UserEntity>
    @Query("SELECT * FROM authentication WHERE username = :userId", nativeQuery = true)
    override fun findById(
            @Param("userId") userId: String
    ) : Optional<UserEntity>*/
}