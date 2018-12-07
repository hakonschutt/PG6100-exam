//package org.bjh.components
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener
//
//class WorkReceiver(val id: String) {
//    @RabbitListener(queues = ["#{queueNameHolder.name}"])
//    fun receive(x: java.lang.Long) {
//        doWork(x)
//    }
//
//    private fun doWork(x: java.lang.Long){
//
//        println("Worker '$id' going to do work with value: $x")
//
//        Thread.sleep(x.toLong())
//    }}