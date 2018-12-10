package org.bjh.component

import org.bjh.dto.VenueDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import kotlin.reflect.KClass

@Component
class HttpService {
    fun <T : VenueDto, MovieDto, RoomDto> getReq(url: String, clazz: KClass<T>): List<T> {

        val template = RestTemplate()

        val data = template.getForObject(url, clazz::class.java)

        return emptyList()

//https://spring.io/guides/gs/consuming-rest/
    }
}