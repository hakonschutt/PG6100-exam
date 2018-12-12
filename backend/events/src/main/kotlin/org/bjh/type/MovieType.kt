package org.bjh.type
/** @author  Kleppa && håkonschutt */
data class MovieType(
        var title: String,
        var poster: String,
        var coverArt: String,
        var trailer: String,
        var overview: String,
        var releaseDate: String,
        var genres: Set<String>,
        var voteCount: Int,
        var voteAverage: String,
        var popularity: String,
        var price: String,
        var id: String
)