package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.bjh.service.EventService
import org.bjh.type.EventInputType
import org.springframework.stereotype.Component
/** @author  Kleppa && håkonschutt */
@Component
class EventMutationResolver( private val eventService: EventService):GraphQLMutationResolver{
    fun  create(event: EventInputType): String{return "not workingTy"}
    fun  update(event: EventInputType): Boolean{return false}
}