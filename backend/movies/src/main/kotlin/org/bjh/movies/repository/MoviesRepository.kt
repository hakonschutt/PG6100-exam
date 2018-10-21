package org.bjh.movies.repository

import org.bjh.movies.entity.MovieEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface MoviesRepository : CrudRepository<MovieEntity, Long>, MoviesRepositoryCustom {
    fun findByTitle(title: String): Iterable<MovieEntity>
}


//TODO: Do I need any custom methods? Time will tell.
@Transactional
interface MoviesRepositoryCustom {
    //Implement extra methods here
}

@Repository
@Transactional
class MoviesRepositoryImpl : MoviesRepositoryCustom{

    //TODO: Consider the use of this
    @Autowired
    private lateinit var em: EntityManager
}