package org.bjh.movies

import org.bjh.movies.dto.MovieDto
import org.bjh.movies.entity.MovieEntity

/**
 * Converter written by lecturer, Andrea Arcuri.
 * Adapted by Bjørn Olav Salvesen.
 */
class MoviesConverter {

    companion object {

        fun transform(entity: MovieEntity): MovieDto {

            return MovieDto(
                    id = entity.id?.toString(),
                    title = entity.title,
                    poster = entity.poster,
                    coverArt = entity.coverArt,
                    trailer = entity.trailer,
                    overview = entity.overview,
                    releaseDate = entity.releaseDate,
                    genres = entity.genres,
                    voteCount = entity.vote_count,
                    voteAverage = entity.vote_average,
                    popularity = entity.popularity,
                    price = entity.price
            ).apply {
                id = entity.id?.toString()
            }
        }

        fun transform(entities: Iterable<MovieEntity>): List<MovieDto> {
            return entities.map { transform(it) }
        }
    }
}
