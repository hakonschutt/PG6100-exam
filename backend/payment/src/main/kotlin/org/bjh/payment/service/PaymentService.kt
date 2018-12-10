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
//    FIXME: Remove _IF_ not to be used.
/*
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

    fun deletePaymentById(id: Long): Boolean {

        if (!paymentRepository.existsById(id))
            return false

        paymentRepository.deleteById(id)
        return true
    }
*/


    fun createPayment(paymentDto: PaymentDto): Long {
        val paymentEntity = PaymentEntity(
                user = paymentDto.user,
                amount = paymentDto.amount!!.toDouble()
        )
        paymentRepository.save(paymentEntity)

        return paymentEntity.id ?: -1L
    }

}