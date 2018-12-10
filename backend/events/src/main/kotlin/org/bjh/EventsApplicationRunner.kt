package org.bjh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@SpringBootApplication
class DatabaseGraphQLApplication


@Bean
fun swaggerApi(): Docket {
    return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.any())
            .build()
}


private fun apiInfo(): ApiInfo {
    return ApiInfoBuilder()
            .title("API for bookings")
            .description("REST API containing all events in house of movies")
            .version("1.0")
            .build()
}

fun main(args: Array<String>) {
    SpringApplication.run(DatabaseGraphQLApplication::class.java, *args)
}