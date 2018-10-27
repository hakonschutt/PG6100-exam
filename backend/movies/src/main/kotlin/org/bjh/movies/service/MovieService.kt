package org.bjh.movies.service

import org.bjh.movies.MoviesConverter
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.entity.MovieEntity
import org.bjh.movies.repository.MoviesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MovieService {

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    fun getById(id: Long): MovieDto {
        val movie = moviesRepository.findById(id).orElse(null)
        //TODO: Does this work if null?
        return MoviesConverter.transform(movie)
    }

    fun getAll(): List<MovieDto> {
        val list = moviesRepository.findAll()
        return MoviesConverter.transform(list)
    }

    fun getAllByTitle(title: String): List<MovieDto> {
        val list = moviesRepository.findAllByTitle(title)
        return MoviesConverter.transform(list)
    }

    fun createMovie(movieDto: MovieDto): Boolean {
        val movieEntity = MovieEntity(
                movieDto.title,
                movieDto.poster!!,
                movieDto.coverArt!!,
                movieDto.trailer!!,
                movieDto.overview!!,
                movieDto.releaseDate!!,
                movieDto.genres!!,
                movieDto.voteCount!!,
                movieDto.voteAverage!!,
                movieDto.popularity!!,
                movieDto.price!!
                )
        moviesRepository.save(movieEntity)

        return movieEntity.id != null
    }

    //TODO: What is the best way to handle non-existing id?
    fun deleteMovieById(id: Long) : Boolean {

        if (!moviesRepository.existsById(id))
            return false

        moviesRepository.deleteById(id)
        return true
    }
}