package org.bjh.movies.service

import org.bjh.movies.MoviesConverter
import org.bjh.movies.dto.MovieDto
import org.bjh.movies.repository.MoviesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MovieService {

    @Autowired
    private lateinit var moviesRepository: MoviesRepository

    fun getAll(): List<MovieDto> {
        val list = moviesRepository.findAll()
        return MoviesConverter.transform(list)
    }

    fun getById(id: Long): MovieDto {
        val movie = moviesRepository.findById(id).get()
        return MoviesConverter.transform(movie)
    }

    fun getAllByTitle(title: String): List<MovieDto> {
        val list = moviesRepository.findAllByTitle(title)
        return MoviesConverter.transform(list)
    }
}