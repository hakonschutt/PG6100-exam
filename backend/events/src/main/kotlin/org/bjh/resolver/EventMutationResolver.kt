package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.google.common.base.Throwables
import graphql.execution.DataFetcherResult
import graphql.servlet.GenericGraphQLError
import org.bjh.service.EventService
import org.bjh.type.EventInputType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolationException

/** @author  Kleppa && håkonschutt */
@Component
class EventMutationResolver( private val eventService: EventService):GraphQLMutationResolver{

    fun  create(event: EventInputType): DataFetcherResult<String> {

        val id = try {

            eventService.create(event)
        } catch (e: Exception) {
            val cause = Throwables.getRootCause(e)
            val msg = if (cause is ConstraintViolationException) {
                "Violated constraints: ${cause.message}"
            }else {
                e.message
            }
            return DataFetcherResult<String>(null, listOf(GenericGraphQLError(msg)))
        }

        return DataFetcherResult(id, listOf())

    }
}