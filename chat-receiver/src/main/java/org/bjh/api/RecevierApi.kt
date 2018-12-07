package org.bjh.api

import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.bjh.wrappers.WrappedResponse
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/chat/receive")
class RestApi {
    @RabbitListener(queues = ["#{queueNameHolder.name}"])
    @ApiResponses(ApiResponse(message = "returns data from amqp queue.", code = 200), ApiResponse(message = "Returns empty list", code = 204))
    fun receiveFromAMQP(@ApiParam("Message from amqp") msg: String): ResponseEntity<WrappedResponse<List<String>>> {

        if (msg.isNotBlank() || msg.isNotEmpty()) {
            return ResponseEntity.status(200).body(WrappedResponse(code = 200, data = listOf(msg), message = null).validated())
        }
        return ResponseEntity.status(204).body(WrappedResponse(code = 204, data = listOf<String>(), message = null).validated())
    }

}