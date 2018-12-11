package org.bjh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.servlet.ObjectMapperConfigurer
import org.springframework.stereotype.Component
// Taken from Andrea's code
@Component
class GraphQlObjectMapperConfigurer : ObjectMapperConfigurer {

    override fun configure(mapper: ObjectMapper) {
        mapper.registerModule(JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}