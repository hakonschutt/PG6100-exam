package main.kotlin.org.bjh.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateController{
    @GetMapping("/")
    fun home():String = "Hello World"
}
