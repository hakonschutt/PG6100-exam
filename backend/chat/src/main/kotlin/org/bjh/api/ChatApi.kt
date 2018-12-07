//package org.bjh.api
//
//import io.swagger.annotations.Api
//import io.swagger.annotations.ApiResponse
//import org.bjh.dto.MessageDto
//import org.bjh.workers.WorkReceiver
//import org.bjh.workers.WorkSender
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//@Api("Api for sending msgs")
//@RestController
//@RequestMapping("/api/chat")
//class WebController {
//
//
//    @PostMapping(path = ["/sender"])
//    @ApiResponse(code = 200, message = "Sends msg to amqp exchange")
//    fun send(@RequestBody msg: MessageDto, @Autowired worker1: WorkSender): ResponseEntity<Void> {
//        worker1.send(listOf(msg))
//        return ResponseEntity.status(204).build()
//    }
//
//    @GetMapping(path = ["/retrieve"])
//    @ApiResponse(code = 200, message = "Retrieve msg to amqp exchange")
//    fun retrieve(): ResponseEntity<Void> {
//
//        return ResponseEntity.status(204).build()
//    }
//
//}