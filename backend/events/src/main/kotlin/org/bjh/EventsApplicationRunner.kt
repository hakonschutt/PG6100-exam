package org.bjh

import com.netflix.config.ConfigurationManager
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
/** @author  Kleppa && håkonschutt */
@SpringBootApplication
@EnableSwagger2
class EventsApplicationRunner {
    //Andrea's code
    init {
        ConfigurationManager.getConfigInstance().apply {
            setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 500)
            setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 2)
            setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50)
            setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000)
        }
    }


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
                .title("GRAPHQL API for events")
                .description("REST API containing all events in house of movies")
                .version("1.0")
                .build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EventsApplicationRunner::class.java, *args)
}