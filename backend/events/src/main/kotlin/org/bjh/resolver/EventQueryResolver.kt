package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.bjh.serivce.EventService
import org.bjh.type.EventType
import org.bjh.type.MovieType
import org.bjh.type.RoomType
import org.bjh.type.VenueType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
/** @author  Kleppa && håkonschutt */
class EventQueryResolver : GraphQLQueryResolver {
    @Autowired
    private lateinit var eventService: EventService

    fun version() = "1.0.0"

    fun allEvents(): List<EventType> {
        return emptyList()
    }

    fun allEventsForVenue(): List<EventType> {
        return emptyList()
    }

    fun allEventsForMovies(): List<EventType> {
        return emptyList()
    }

//    fun eventById(id: Long): EventType {
//        return EventType(
//                id = 1,
//                date = ZonedDateTime.now(),
//                movieId = MovieType(
//                        title = "test",
//                        poster = "ok",
//                        voteAverage = "3",
//                        voteCount = 2,
//                        coverArt = "ok" ,
//                        trailer = "nop",
//                        overview = "kk",
//                        releaseDate = "oki",
//                        genres = setOf("test-genre"),
//                        price = "10",
//                        popularity = "ok",
//                        id = "12312"
//                ),
//                venueId = VenueType(
//                        name = "test",
//                        id = "test",
//                        geoLocation = "test",
//                        address = "test",
//                        rooms = setOf(RoomType(
//                                id = "test - room",
//                                name = "Test",
//                                rows = 10,
//                                columns = 10
//                        ))
//
//                ),
//                roomId = RoomType( id = "test - room",
//                        name = "Test",
//                        rows = 10,
//                        columns = 10),
//                rows = 10,
//                columns = 10
//        )
//    }
}