package org.bjh.component

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.bjh.wrappers.WrappedResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import rx.Observable
/** @author  Kleppa && håkonschutt */

@Component
class HttpService {
   fun getReq(url: String): WrappedResponse<*> = (HystrixCall(url).execute())
    private inner class HystrixCall(private val url: String)
        : HystrixCommand<WrappedResponse<*>>(HystrixCommandGroupKey.Factory.asKey("Interactions with $url")) {
        override fun run(): WrappedResponse<*>? = RestTemplate().getForObject(url, WrappedResponse::class.java)
        override fun getFallback(): WrappedResponse<*> = WrappedResponse<Any>(code = 500, message = "server at $url timed out or threw an exception").validated()
    }
}