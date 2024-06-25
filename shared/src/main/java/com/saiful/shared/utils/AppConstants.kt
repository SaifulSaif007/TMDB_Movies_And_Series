package com.saiful.shared.utils

object AppConstants {

    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val ORIGINAL_SIZE = "w1280"
    const val BACKDROP_SIZE = "w780" //w1280 // original
    const val POSTER_SIZE = "w342" // w500 // w780 // original

    const val YOUTUBE_IMAGE_URL_PREFIX = "https://img.youtube.com/vi/"
    const val YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg"

    const val SEARCHED_QUERY = "searched_query"
}

object RequestKeys {
    const val MOVIE_REQUEST_KEY = "movie_request_key"
    const val SERIES_REQUEST_KEY = "series_request_key"
    const val PERSON_REQUEST_KEY = "person_request_key"
}

object BundleKeyS {
    const val MOVIE_ID = "movie_id"
    const val SHOW_ID = "show_id"
    const val PERSON_ID = "person_id"
}