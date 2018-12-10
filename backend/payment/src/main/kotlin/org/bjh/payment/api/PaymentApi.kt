package org.bjh.payment.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.dto.PaymentDto
import org.bjh.payment.service.PaymentService
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "/api/payments", description = "For completing purchases.")
@RequestMapping(
        path = ["/api/payments"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class PaymentApi {

    @Autowired
    private lateinit var paymentService: PaymentService

    private val basePath = "/api/payments"

    @ApiOperation("Create a payment")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun createPayment(@ApiParam("Information for payment - User, price and authorization token.")
                    @RequestBody paymentDto: PaymentDto): ResponseEntity<WrappedResponse<Unit>> {

        if (paymentDto.paymentAuthorizationToken == null)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "Unusable payment authorization token.")
                            .validated()
            )

        val createdId = paymentService.createPayment(paymentDto)

        if (createdId == -1L)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "Unable to create a payment.")
                            .validated()
            )

        return ResponseEntity.status(201).body(
                WrappedResponse<Unit>(
                        code = 201,
                        message = "Payment was completed.")
                        .validated()
        )
    }
}