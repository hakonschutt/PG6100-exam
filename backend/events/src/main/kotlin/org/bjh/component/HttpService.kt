package org.bjh.component

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.bjh.dto.MovieDto
import org.bjh.dto.VenueDto
import org.bjh.wrappers.WrappedResponse
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import rx.Observable
import java.net.URI
import kotlin.reflect.KClass

@Component
class HttpService {
    fun getReq(url: String): WrappedResponse<*> = HystrixCall(url).execute()
    private inner class HystrixCall(private val url: String)
        : HystrixCommand<WrappedResponse<*>>(HystrixCommandGroupKey.Factory.asKey("Interactions with $url")) {
        override fun run(): WrappedResponse<*>? = RestTemplate().getForObject(url, WrappedResponse::class.java)
        override fun getFallback(): WrappedResponse<*> = WrappedResponse<Any>(code = 500, message = "server timed out or threw an exception").validated()

    }
}