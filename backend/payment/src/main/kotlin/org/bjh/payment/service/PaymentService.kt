package org.bjh.payment.service

import org.bjh.dto.PaymentDto
import org.bjh.pagination.PageDto
import org.bjh.payment.converter.PaymentConverter
import org.bjh.payment.entity.PaymentEntity
import org.bjh.payment.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PaymentService {

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    fun getById(id: Long): PaymentDto {
        val movie = paymentRepository.findById(id).orElse(null) ?: return PaymentDto(null, "")
        return PaymentConverter.transform(movie)
    }

    @Cacheable("paymentsCache")
    fun getAll(offset: Int, limit: Int): PageDto<PaymentDto> {
        val list = paymentRepository.findAll(offset, limit)
        return PaymentConverter.transform(list)
    }

    fun getAllById(id: Long) : PageDto<PaymentDto> {
        val movie = paymentRepository.findAllById(id)
        return PaymentConverter.transform(movie)
    }

    fun save(payment: PaymentDto): PaymentEntity {

        val entity = PaymentEntity(
                id = payment.id?.toLong(),
                user = payment.user,
                price = payment.price!!.toDouble())

        return paymentRepository.save(entity)
    }

    fun createPayment(paymentDto: PaymentDto): Long {
        val paymentEntity = PaymentEntity(
                paymentDto.user,
                paymentDto.price!!.toDouble(),
                paymentDto.id!!.toLong()
        )
        paymentRepository.save(paymentEntity)

        return paymentEntity.id ?: -1L
    }

    fun deleteMovieById(id: Long): Boolean {

        if (!paymentRepository.existsById(id))
            return false

        paymentRepository.deleteById(id)
        return true
    }
}