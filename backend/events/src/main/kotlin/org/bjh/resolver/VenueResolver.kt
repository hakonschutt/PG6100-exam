package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.bjh.component.HttpService
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.bjh.wrappers.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
/** @author  Kleppa && håkonschutt */

@Component
class VenueResolver : GraphQLResolver<VenueType> {
    @Autowired
    private lateinit var httpService:HttpService
    fun rooms(venue : VenueType): Set<RoomType>? {
        // TODO: Create HTTP request
        val req = (httpService.getReq("http://localhost:8080/api/venues/${venue.id}/rooms") as WrappedResponse<Any>).data
        println("This is the wrapped response from mathapi ${req}")
        return null
    }
}