package org.bjh.repository

import org.bjh.entity.BookingEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : PagingAndSortingRepository<BookingEntity, Long> {
    @Query("SELECT * FROM bookings OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
        @Param("offset") offset: Int = 0,
        @Param("limit") limit: Int = 20
    ) : List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE event = :eventId OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAllByEventId(
        @Param("eventId") eventId: Long,
        @Param("offset") offset: Int = 0,
        @Param("limit") limit: Int = 20
    ) : List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE user = :userId OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAllByUserId(
        @Param("userId") userId: Long,
        @Param("offset") offset: Int = 0,
        @Param("limit") limit: Int = 20
    ) : List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE user = :userId AND event = :eventId OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAllByEventIdAndUserId(
        @Param("eventId") eventId: Long,
        @Param("userId") userId: Long,
        @Param("offset") offset: Int = 0,
        @Param("limit") limit: Int = 20
    ): List<BookingEntity>

    @Query("SELECT COUNT(*) FROM bookings WHERE event = :eventId", nativeQuery = true)
    fun countByEventId(
        @Param("eventId") eventId: Long
    ): Int

    @Query("SELECT COUNT(*) FROM bookings WHERE user = :userId", nativeQuery = true)
    fun countByUserId(
        @Param("userId") userId: Long
    ): Int

    @Query("SELECT COUNT(*) FROM bookings WHERE user = :userId AND event = :eventId", nativeQuery = true)
    fun countByEventIdAndUserId(
        @Param("eventId") eventId: Long,
        @Param("userId") userId: Long
    ): Int
}