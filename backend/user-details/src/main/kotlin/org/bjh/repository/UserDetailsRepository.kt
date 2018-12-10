package org.bjh.repository

import org.bjh.entity.UserDetailEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDetailsRepository : PagingAndSortingRepository<UserDetailEntity,String> {

    @Query("SELECT * FROM user_detail OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
            @Param("offset") offset:Int=0,
            @Param ("limit")limit:Int=20)
            :List<UserDetailEntity>
    @Query("SELECT * FROM user_detail WHERE email = :userId", nativeQuery = true)
    override fun findById(
            @Param("userId") userId: String
    ) : Optional<UserDetailEntity>
}