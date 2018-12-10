package org.bjh.payment.api

import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "/api/payments", description = "For completing purchases.")
@RequestMapping(
        path = ["/api/payments"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class PaymentApi {

    

}