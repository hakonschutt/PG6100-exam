package org.bjh.converter

import org.bjh.dto.MovieDto
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.bjh.entity.EventEntity
import org.bjh.type.*

/** @author  Kleppa && håkonschutt */

class EventConverter {

    companion object {
        // force unwrapping since we are not creating new dtos only fetching from movieservice

        fun transformMovieDtoToType(movieDto: MovieDto): MovieType = MovieType(
                id = movieDto.id!!,
                title = movieDto.title!!,
                poster = movieDto.poster!!,
                coverArt = movieDto.coverArt!!,
                trailer = movieDto.trailer!!,
                overview = movieDto.overview!!,
                releaseDate = movieDto.releaseDate!!,
                genres = movieDto.genres!!,
                voteCount = movieDto.voteCount!!,
                voteAverage = movieDto.voteAverage!!,
                popularity = movieDto.popularity!!,
                price = movieDto.price!!
        )


        fun transformEventEntityToType(eventEntity: EventEntity): EventType = EventType(
                id = eventEntity.id!!,
                date = eventEntity.date,
                venueId = eventEntity.venueId,
                movieId = eventEntity.movieId,
                columns = eventEntity.columns!!,
                rows = eventEntity.rows!!,
                roomId = eventEntity.roomId.toString()
        )


        fun transformVenueDtoToType(venueDto: VenueDto): VenueType = VenueType(
                id = venueDto.id!!,
                geoLocation = venueDto.geoLocation!!,
                address = venueDto.address!!,
                name = venueDto.name!!
        )


        fun transformRoomDtoToType(roomDto: RoomDto): RoomType = RoomType(
                id = roomDto.id!!,
                name = roomDto.name!!,
                rows = roomDto.rows!!,
                columns = roomDto.columns!!
        )

        fun transformEventEntityToType(listEventEnt: List<EventEntity>): List<EventType> = listEventEnt.map { transformEventEntityToType(it) }
        fun transformEventInputToEntity(eventInputType: EventInputType): EventEntity = EventEntity(
                id = null,
                date = eventInputType.date,
                movieId = eventInputType.movieId,
                venueId = eventInputType.venueId,
                roomId = eventInputType.roomId,
                rows = eventInputType.rows,
                columns = eventInputType.columns

        )
    }

}