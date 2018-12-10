package org.bjh.payment.repository

import org.bjh.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CrudRepository<PaymentEntity, Long> {

    @Query("SELECT * FROM payment OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
            @Param("offset") offset: Int = 0,
            @Param("limit") limit: Int = 20)
            : List<PaymentEntity>

    @Query("SELECT m FROM payment m WHERE m.id = :id")
    fun findAllById( @Param("id")id:Long ):List<PaymentEntity>
}
