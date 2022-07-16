package com.saiful.movie.model

data class PopularMovies(
    val page: Int,
    val results: List<Movies>,
    val totalPages: Int,
    val totalResults: Int
)