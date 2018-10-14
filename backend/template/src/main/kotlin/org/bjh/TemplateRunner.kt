package main.kotlin.org.bjh
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TemplateRunner
 fun main(args: Array<String>) {
    SpringApplication.run(TemplateRunner::class.java, *args)
}
