package org.bjh.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.bjh.service.EventService
import org.bjh.type.EventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
/** @author  Kleppa && håkonschutt */
class EventQueryResolver : GraphQLQueryResolver {
    @Autowired
    private lateinit var eventService: EventService

    fun version() = "1.0.0"

    fun allEvents(venue: String?, movie: String?): List<EventType> {
       return  when {
            venue.isNullOrEmpty() && !movie.isNullOrEmpty() -> eventService.findAllEventsByMovie(movie)
            (!venue.isNullOrEmpty() && movie.isNullOrEmpty()) -> eventService.findAllEventsByVenue(venue)
            else -> eventService.findAll()
        }
    }

}
/*EventType(
                id = 1,
                date = ZonedDateTime.now(),
                movieId = MovieType(
                        title = "test",
                        poster = "ok",
                        voteAverage = "3",
                        voteCount = 2,
                        coverArt = "ok" ,
                        trailer = "nop",
                        overview = "kk",
                        releaseDate = "oki",
                        genres = setOf("test-genre"),
                        price = "10",
                        popularity = "ok",
                        id = "12312"
                ),
                venueId = VenueType(
                        name = "test",
                        id = "test",
                        geoLocation = "test",
                        address = "test",
                        rooms = setOf(RoomType(
                                id = "test - room",
                                name = "Test",
                                rows = 10,
                                columns = 10
                        ))

                ),
                roomId = RoomType( id = "test - room",
                        name = "Test",
                        rows = 10,
                        columns = 10),
                rows = 10,
                columns = 10
        )*/