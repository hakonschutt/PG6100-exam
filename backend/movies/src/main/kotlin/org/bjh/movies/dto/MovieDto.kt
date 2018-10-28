package org.bjh.movies.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 * @author bjornosal
 */
@ApiModel(description = "Information about a movie")
data class MovieDto(

        @ApiModelProperty("The title of the movie")
        var title: String? = null,

        @ApiModelProperty("Link to the poster for the movie")
        var poster: String? = null,

        @ApiModelProperty("Link to the cover art for the movie")
        var coverArt: String? = null,

        @ApiModelProperty("The trailer for the movie")
        var trailer: String? = null,

        @ApiModelProperty("The overview of the movie")
        var overview: String? = null,

        @ApiModelProperty("The releasedate of the movie")
        var releaseDate: Date? = null,

        @ApiModelProperty("The genres of the movie")
        var genres: Set<String>? = null,

        @ApiModelProperty("Vote count for the movie")
        var voteCount: Int? = null,

        @ApiModelProperty("Average vote score for the movie")
        var voteAverage: Double? = null,

        @ApiModelProperty("Popularity of the movie")
        var popularity: Double? = null,

        @ApiModelProperty("Price of the movie")
        var price: Double? = null,

        @ApiModelProperty("The id of the movie")
        var id: String? = null
)