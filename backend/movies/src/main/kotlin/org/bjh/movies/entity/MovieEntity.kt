package org.bjh.movies.entity

import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank


/**
 * @author bjornosal
 */
@Entity
class MovieEntity(

        @get:NotBlank
        var title: String?,
        var poster: String? = null,
        var coverArt: String? = null,
        var trailer: String? = null,
        var overview: String? = null,
        var releaseDate: Date? = null,
        @get:ElementCollection
        var genres: Set<String>? = null,
        var vote_count: Int?,
        var vote_average: Double? = null,
        var popularity: Double? = null,
        var price: Double? = null,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null
)