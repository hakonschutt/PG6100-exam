package org.bjh.payment.converter

import org.bjh.dto.PaymentDto
import org.bjh.pagination.PageDto
import org.bjh.payment.entity.PaymentEntity

/**
 * Converter written by lecturer, Andrea Arcuri.
 * Adapted by Bjørn Olav Salvesen.
 */
class PaymentConverter {
    //    FIXME: Remove _IF_ not to be used.
/*
    companion object {

        fun transform(entity: PaymentEntity): PaymentDto {

            return PaymentDto(
                    id = entity.id?.toString(),
                    user = entity.user,
                    amount = entity.amount.toString()
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Iterable<PaymentEntity>): PageDto<PaymentDto> {
            val payments = entities.map { transform(it) }

            return PageDto(list = payments, totalSize = payments.size)
        }
    }*/
}
