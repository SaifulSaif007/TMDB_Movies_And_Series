package com.saiful.shared.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.shared.R

fun ImageView.loadBackDropSizeImage(url: String?, drawable: Int = R.drawable.no_image_icon) {
    Glide.with(this.context)
        .load(AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

fun ImageView.loadPosterSizeImage(url: String?, drawable: Int = R.drawable.no_image_icon) {
    Glide.with(this.context)
        .load(AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

fun ImageView.loadOriginalSizeImage(url: String?, drawable: Int = R.drawable.no_image_icon) {
    Glide.with(this.context)
        .load(AppConstants.IMAGE_BASE_URL + AppConstants.ORIGINAL_SIZE + url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

fun ImageView.loadYoutubeImage(key: String?) {
    Glide.with(this.context)
        .load(AppConstants.YOUTUBE_IMAGE_URL_PREFIX + key + AppConstants.YOUTUBE_IMAGE_URL_SUFFIX)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(drawable)
        .into(this)
}