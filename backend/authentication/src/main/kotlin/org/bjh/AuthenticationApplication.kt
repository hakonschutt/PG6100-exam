package org.bjh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication
@EnableSwagger2
class AuthenticationApplication {

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
                .title("API for authentication")
                .description("REST API for authentication to House of Movies")
                .version("1.0")
                .build()
    }
}

fun main(args: Array<String>) {
    runApplication<AuthenticationApplication>(*args)
}