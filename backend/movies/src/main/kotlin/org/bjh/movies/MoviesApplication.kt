package org.bjh.movies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableSwagger2
class MoviesApplication {

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
                .title("API for REST Pokemon")
                .description("This is a REST API containing all Pokémon from Gen 1")
                .version("1.0")
                .build()
    }

    fun main(args: Array<String>) {
        runApplication<MoviesApplication>(*args)
    }
}
