package org.bjh.movies.service

import org.bjh.movies.MoviesConverter
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class MovieService {

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    fun getById(id: Long): MovieDto {
        val movie = moviesRepository.findById(id).orElse(null) ?: return MovieDto(null)
        //TODO: Does this work if null?
        return MoviesConverter.transform(movie)
    }

    fun getAll(offset: Int, limit: Int): List<MovieDto> {
        val list = moviesRepository.findAll(offset, limit)
        return MoviesConverter.transform(list)
    }

    fun save(movie: MovieDto): MovieEntity {

        val entity = MovieEntity(
                id = movie.id!!.toLong(),
                title = movie.title!!,
                poster = movie.poster!!,
                coverArt = movie.coverArt!!,
                trailer = movie.trailer!!,
                overview = movie.overview!!,
                releaseDate = LocalDate.parse(movie.releaseDate!!),
                genres = movie.genres!!,
                voteCount = movie.voteCount!!,
                voteAverage = movie.voteAverage!!.toDouble(),
                popularity = movie.popularity!!.toDouble(),
                price = movie.price!!.toDouble())

        return moviesRepository.save(entity)
    }

    fun createMovie(movieDto: MovieDto): Long {
//TODO: Is this the correct way to change the values?
        val movieEntity = MovieEntity(
                movieDto.title,
                movieDto.poster!!,
                movieDto.coverArt!!,
                movieDto.trailer!!,
                movieDto.overview!!,
                LocalDate.parse(movieDto.releaseDate)!!,
                movieDto.genres!!,
                movieDto.voteCount!!,
                movieDto.voteAverage!!.toDouble(),
                movieDto.popularity!!.toDouble(),
                movieDto.price!!.toDouble()
        )
        moviesRepository.save(movieEntity)

        return movieEntity.id ?: -1L
    }

    //TODO: What is the best way to handle non-existing id?
    fun deleteMovieById(id: Long): Boolean {

        if (!moviesRepository.existsById(id))
            return false

        moviesRepository.deleteById(id)
        return true
    }
}