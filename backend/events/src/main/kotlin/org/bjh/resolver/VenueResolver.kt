package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.springframework.stereotype.Component

@Component
class VenueResolver : GraphQLResolver<VenueType> {
    fun rooms(venuee : VenueType): RoomType? {
        // TODO: Create HTTP request
        return null
    }
}