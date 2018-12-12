package org.bjh.payment.api

import com.google.gson.JsonObject
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.bjh.dto.PaymentDto
import org.bjh.payment.service.PaymentService
import org.bjh.wrappers.WrappedResponse
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.ConnectException

@Api(value = "/api/payments", description = "For completing purchases.")
@RequestMapping(
        path = ["/api/payments"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class PaymentApi {

    @Autowired
    private lateinit var paymentService: PaymentService

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange

    private val basePath = "/api/payments"

    @ApiOperation("Create a payment")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun createPayment(@ApiParam("Information for payment - User, amount and authorization token.")
                      @RequestBody paymentDto: PaymentDto): ResponseEntity<WrappedResponse<Unit>> {

        if(paymentDto.amount == null || paymentDto.amount!!.toDoubleOrNull() == null)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "Invalid amount.")
                            .validated()
            )

        if (paymentDto.paymentAuthorizationToken == null)
            return ResponseEntity.status(400).body(
                    WrappedResponse<Unit>(
                            code = 400,
                            message = "Unusable payment authorization token.")
                            .validated()
            )

        val createdId = paymentService.createPayment(paymentDto)

        val message = JsonObject()
        message.addProperty("user", paymentDto.user)
        message.addProperty("success", true)

        try {
            rabbitTemplate.convertAndSend(fanout.name, "", message.toString())
        } catch (ce: Exception) {
            return ResponseEntity.status(201).body(
                    WrappedResponse<Unit>(
                            code = 201,
                            message = "Payment was completed, but not sent to Booking." +
                                    " Call with id: $createdId to manually fix it.")
                            .validated()
            )
        }

        return ResponseEntity.status(201).body(
                WrappedResponse<Unit>(
                        code = 201,
                        message = "Payment was completed.")
                        .validated()
        )
    }
}