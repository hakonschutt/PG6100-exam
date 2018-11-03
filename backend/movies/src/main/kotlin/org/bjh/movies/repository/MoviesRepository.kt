package org.bjh.movies.repository

import org.bjh.movies.entity.MovieEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface MoviesRepository : CrudRepository<MovieEntity, Long>, MoviesRepositoryCustom {

    @Query("SELECT * FROM movies OFFSET :offset LIMIT :limit", nativeQuery = true)
    fun findAll(
            @Param("offset") offset: Int = 0,
            @Param("limit") limit: Int = 20)
            : List<MovieEntity>

    @Query("SELECT m FROM movies m WHERE m.id = :id")
    fun findAllById( @Param("id")id:Long ):List<MovieEntity>
}


//TODO: Do I need any custom methods? Time will tell.
@Transactional
interface MoviesRepositoryCustom {
    //Implement extra methods here
}

@Repository
@Transactional
class MoviesRepositoryImpl : MoviesRepositoryCustom {

    //TODO: Check up on this.
    @Autowired
    private lateinit var em: EntityManager
}